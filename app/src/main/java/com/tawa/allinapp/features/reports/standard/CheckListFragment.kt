package com.tawa.allinapp.features.reports.standard

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentChecklistBinding
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import java.io.File
import android.os.Environment
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.databinding.HeaderReportBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList


class CheckListFragment: BaseFragment() {

    private lateinit var binding: FragmentChecklistBinding
    private lateinit var headerReportBinding: HeaderReportBinding
    private val TAKE_PHOTO_REQUEST = 101
    var photoFile: File? = null
    val CAPTURE_IMAGE_REQUEST = 1
    var mCurrentPhotoPath: String? = null
    private lateinit var checkListViewModel: CheckListViewModel
    private var  listRadioButton = ArrayList<RadioButton>()
    private var  listCheckBox = ArrayList<CheckBox>()
    private var  listInput = ArrayList<EditText>()
    private lateinit var  listInit:List<Question>
    private var state:Boolean = false
    private var verify:Boolean = false
    private  var idPhoto = ""
    private var idQuestion = ""
    private var idReport=""
    private var typeReport = ""

    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    var urlImage = ""

    private  var statePhoto = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChecklistBinding.inflate(inflater)


        checkListViewModel = viewModel(viewModelFactory) {
            observe(questions, { it?.let {
                listInit = it.sortedBy { it.order }
                listInit = listInit.filter { filter -> filter.questionName!="TIENE FOTO"}
                for(list in listInit)
                   showQuestions(list.objectType,list.id,list.questionName,list.order)
            } })
            observe(stateReport,{it?.let {
                getLastLocation()
                if(it.isNotEmpty())
                {
                    typeReport=it
                    checkListViewModel.getQuestions(idReport)
                }
            } })
            observe(successSyncReportStandard,{it?.let {
                    if(it)
                        activity?.onBackPressed()
            } })
            observe(answersRadio, { it?.let {
                if(orderRadio.value!! >0)
                    addAnswersRadio(it,binding.contentCheckList,nameQuestion.value!!,orderRadio.value!!)
            } })

            observe(answersCheck, { it?.let {
                if(orderCheckBox.value!! >0)
                    addAnswersCheck(it,binding.contentCheckList,nameQuestion.value!!,orderCheckBox.value!!)
            } })
            observe(answersInput, { it?.let {
                if(orderInput.value!! >0)
                    addAnswersInput(it,binding.contentCheckList,nameQuestion.value!!,orderInput.value!!)

            } })

            observe(answersPhoto, { it?.let {
                binding.tvPhoto.text = nameQuestion.value
                idPhoto = it[0].id
                idQuestion = it[0].idQuestion
                if( it[0].data!="VACIO" &&  it[0].data!="")
                {
                    binding.checkListPhoto.isVisible = true
                    binding.deletePhoto.isVisible = true
                    urlImage =  it[0].data
                    val imageUri = Uri.fromFile(File( it[0].data))
                    binding.checkListPhoto.setImageURI(imageUri,"")
                }
            } })
            observe(stateRadio, { it?.let {
                if(it)
                    findRadioButton()
            } })
            observe(stateCheck, { it?.let {
                if(it)
                    findCheckBox()
            } })
            observe(stateInput, { it?.let {
               if(it)
                   findInput()
            } })
            observe(updateReportState,{it?.let {
            } })
            failure(failure, { it?.let {
                Toast.makeText(context,"Debe seleccionar un punto de venta",Toast.LENGTH_SHORT).show()
            }})
        }

        arguments?.getString("id").toString().also {idRep->
            idReport=idRep
            checkListViewModel.getStateReport(idReport,1)
        }

        arguments?.getString("name").toString().also {
            binding.tvTitleStandard.text = it
        }

        binding.btnTakePhoto.setOnClickListener{
           validatePermissions()
          //captureImage()
        }
        binding.deletePhoto.setOnClickListener{
            binding.checkListPhoto.visibility = View.GONE
            binding.deletePhoto.visibility = View.GONE
            statePhoto = 0
            urlImage=""
        }
        binding.btnSaveReport.setOnClickListener{
            //checkListViewModel.updateState(true)
            if(typeReport!="Terminado") {
                getLastLocation()
                //checkListViewModel.updateState(true, true)
                checkListViewModel.updateStateReport(idReport, "En proceso","Terminado")
                checkListViewModel.updateReportPv(idReport,"En proceso","Terminado",Calendar.getInstance().toInstant().toString(),latitude,longitude)
                for (radio in listRadioButton) {
                    val tag = radio.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],radio.isChecked.toString(),urlImage)
                //checkListViewModel.updateAnswers(tag[0], radio.isChecked.toString())
                }
                for (check in listCheckBox) {
                    val tag = check.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],check.isChecked.toString(),urlImage)
                //checkListViewModel.updateAnswers(tag[0], check.isChecked.toString())
                }
                for (input in listInput) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())

                }
                checkListViewModel.setAnswerPv(idPhoto,idQuestion,urlImage,urlImage)
                //checkListViewModel.updateAnswers(idPhoto, urlImage)

                findElements()

                showConfirmSync()
            }
            else
                Toast.makeText(context,"Ya se registró",Toast.LENGTH_SHORT).show()
        }
        binding.btnBackCheckList.setOnClickListener{
            activity?.onBackPressed()
        }
        binding.btnBr.setOnClickListener {
            if(typeReport!="Terminado") {
                //checkListViewModel.updateState(true, false)
                checkListViewModel.updateStateReport(idReport, "En proceso","Borrador")
                checkListViewModel.updateReportPv(idReport,"En proceso","Borrador","","","")
                for (radio in listRadioButton) {
                    val tag = radio.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],radio.isChecked.toString(),urlImage)
                   // checkListViewModel.updateAnswers(tag[0], radio.isChecked.toString())
                }
                for (check in listCheckBox) {
                    val tag = check.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],check.isChecked.toString(),urlImage)
                   // checkListViewModel.updateAnswers(tag[0], check.isChecked.toString())
                }
                for (input in listInput) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())

                }
                //checkListViewModel.updateAnswers(idPhoto, urlImage)
                checkListViewModel.setAnswerPv(idPhoto,idQuestion,urlImage,urlImage)

                activity?.onBackPressed()
            }
            else
                Toast.makeText(context,"Ya se registró",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun findElements(){
        checkListViewModel.startRadio()
        checkListViewModel.startCheck()
        checkListViewModel.startInput()
    }

    private fun findRadioButton(){
        for(radio in listRadioButton)
        {
            if(radio.isChecked)
            {
                val tag = radio.tag as ArrayList<String>
                checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],radio.text.toString(),urlImage)
            }
        }
    }

    private fun findCheckBox(){
        for(check in listCheckBox)
        {
            if(check.isChecked)
            {
                val tag = check.tag as ArrayList<String>
                checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],check.text.toString(),urlImage)
            }
        }
    }

    private fun findInput(){
        for(input in listInput)
        {
            val tag = input.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.text.toString(),urlImage)

        }
    }

    private fun showQuestions(type:String,id:String,questionName:String,order:Int){
                if(type=="CHECK")
                    checkListViewModel.getAnswersRadio(id,questionName,order)
                if(type=="CHECK LIST")
                    checkListViewModel.getAnswersCheck(id,questionName,order)
                if(type=="INPUT")
                    checkListViewModel.getAnswersInput(id,questionName,order)
                if(type=="FOTO")
                    checkListViewModel.getAnswersPhoto(id,questionName)
    }

    private fun addAnswersRadio(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order: Int){
            params.setMargins(0,0, 0, 10f.toDips().toInt())
            val linearL = LinearLayout(context)
            linearL.orientation = LinearLayout.VERTICAL
            val textView  = TextView(context)
            textView.text = nameQ
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            linearL.addView(textView)

            val radioGroup = RadioGroup(context)
            for(list in listAnswers)
            {
                val radioButton  = RadioButton(context)
                val listTag =ArrayList<String>(2)
                radioButton.text = list.answerName
                listTag.add(list.id)
                listTag.add(list.idQuestion)
                listTag.add(nameQ)
                radioButton.tag = listTag
                if(typeReport!="0")
                    radioButton.isChecked = list.data.toBoolean()
                listRadioButton.add(radioButton)
                radioGroup.addView(radioButton)
            }
           linearL.addView(radioGroup)
           linearL.layoutParams = params
           linear.addView(linearL,order)

    }

    private fun addAnswersCheck(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order: Int){
            params.setMargins(0,0, 0, 20f.toDips().toInt())
            val linearL = LinearLayout(context)
            linearL.orientation = LinearLayout.VERTICAL
            val textView  = TextView(context)
            textView.text = nameQ
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            linearL.addView(textView)
            for(list in listAnswers)
            {
                val checkBox  = CheckBox(context)
                val listTag =ArrayList<String>(2)
                checkBox.text = list.answerName
                listTag.add(list.id)
                listTag.add(list.idQuestion)
                listTag.add(nameQ)
                checkBox.tag = listTag
                if(typeReport!="0")
                    checkBox.isChecked=list.data.toBoolean()

                listCheckBox.add(checkBox)
                linearL.addView(checkBox)
            }
            linearL.layoutParams = params
            linear.addView(linearL,order)

    }

    private fun addAnswersInput(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order: Int){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)

        for(list in listAnswers)
        {
            val editText  = EditText(context)
            editText.setBackgroundResource(R.drawable.rounded)
            editText.setPadding((16f).toDips().toInt(),0,(16f).toDips().toInt(),0)
            editText.height = (50f).toDips().toInt()
            val listTag =ArrayList<String>(2)
            editText.hint = list.answerName.toLowerCase()
            listTag.add(list.id)
            listTag.add(list.idQuestion)
            listTag.add(nameQ)
            editText.tag = listTag
            if(typeReport!="0")
                editText.setText(list.data)
            listInput.add(editText)
            linearL.addView(editText)
        }
        linearL.layoutParams = params
        linear.addView(linearL,order)

    }

    private fun showConfirmSync(){
        val dialog = ConfirmSyncDialogFragment()
        dialog.listener = object : ConfirmSyncDialogFragment.Callback{
            override fun onClick() {
                getLastLocation()
                checkListViewModel.syncStandardReports(idReport,latitude,longitude)
            }

            override fun onBack() {
                activity?.onBackPressed()
            }

        }
        dialog.show(childFragmentManager,"")
    }

    private fun Float.toDips() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    private fun validatePermissions() {
        Dexter.withActivity(requireActivity())
            .withPermissions(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    launchCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }
            })
            .check()
    }


    private fun launchCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create the File where the photo should go
        try {
            photoFile = createImageFile()
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "com.tawa.captureimage.fileprovider",
                    photoFile!!
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
            }
        } catch (ex: Exception) {
            // Error occurred while creating the File
            displayMessage(requireContext(), ex.message.toString())
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            binding.checkListPhoto.isVisible = true
            binding.deletePhoto.isVisible = true
            urlImage = photoFile!!.absolutePath
            val imageUri = Uri.fromFile(File(urlImage))
            binding.checkListPhoto.setImageURI(imageUri,"")
            statePhoto = 1
        } else {
            displayMessage(requireContext(), "Request cancelled or something went wrong.")
        }
    }


}