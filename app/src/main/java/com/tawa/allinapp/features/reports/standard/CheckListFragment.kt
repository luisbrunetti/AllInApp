package com.tawa.allinapp.features.reports.standard

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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


class CheckListFragment: BaseFragment() {

    private lateinit var binding: FragmentChecklistBinding
    private val TAKE_PHOTO_REQUEST = 101
    private var mCurrentPhotoPath: String = ""
    private lateinit var checkListViewModel: CheckListViewModel
    private var  listRadioButton = ArrayList<RadioButton>()
    private var  listCheckBox = ArrayList<CheckBox>()
    private var  listInput = ArrayList<EditText>()
    private lateinit var  listInit:List<Question>

    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    var urlImage = ""

    private  var statePhoto = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChecklistBinding.inflate(inflater)
        val id = arguments?.getString("id")
        //Toast.makeText(context,id,Toast.LENGTH_SHORT).show()

        checkListViewModel = viewModel(viewModelFactory) {
            observe(questions, { it?.let {
                listInit = it.sortedBy { it.order }
                for(list in listInit)
                   showQuestions(list.objectType,list.id,list.questionName,list.order)
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
        }

        checkListViewModel.getQuestions()
        binding.btnTakePhoto.setOnClickListener{
           validatePermissions()
        }
        binding.deletePhoto.setOnClickListener{
            binding.checkListPhoto.visibility = View.GONE
            binding.deletePhoto.visibility = View.GONE
            statePhoto = 0
        }
        binding.btnSaveReport.setOnClickListener{
           findElements()
            Toast.makeText(context,urlImage,Toast.LENGTH_SHORT).show()
        }
        binding.btnBackCheckList.setOnClickListener{
            activity?.onBackPressed()
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
                checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],radio.text.toString())
            }
        }
    }

    private fun findCheckBox(){
        for(check in listCheckBox)
        {
            if(check.isChecked)
            {
                val tag = check.tag as ArrayList<String>
                checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],check.text.toString())
            }
        }
    }

    private fun findInput(){
        for(input in listInput)
        {
            val tag = input.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.text.toString())

        }
    }

    private fun showQuestions(type:String,id:String,questionName:String,order:Int){
                if(type=="CHECK")
                    checkListViewModel.getAnswersRadio(id,questionName,order)
                if(type=="CHECK LIST")
                    checkListViewModel.getAnswersCheck(id,questionName,order)
                if(type=="INPUT")
                    checkListViewModel.getAnswersInput(id,questionName,order)
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
            listInput.add(editText)
            linearL.addView(editText)
        }
        linearL.layoutParams = params
        linear.addView(linearL,order)

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

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == TAKE_PHOTO_REQUEST) {
            //processCapturedPhoto()
            val bitmap = data!!.extras?.get("data") as Bitmap
            val uri = getImageUri(activity?.applicationContext!!, bitmap)
            val height =200
            val width = 300
            val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(width, height))
                .build()
            val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(binding.checkListPhoto?.controller)
                .setImageRequest(request)
                .build()
            binding.checkListPhoto?.controller = controller
            //Toast.makeText(context, file.toString(), Toast.LENGTH_SHORT).show()
            binding.checkListPhoto.visibility = View.VISIBLE
            binding.deletePhoto.visibility = View.VISIBLE
            statePhoto = 1
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        val image = Bitmap.createScaledBitmap(inImage!!, 300, 300, true)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, image, "picture", null)
        val cursor = requireActivity().contentResolver.query(
            Uri.parse(path),
            Array(1) {android.provider.MediaStore.Images.ImageColumns.DATA},
            null, null, null)
        cursor?.moveToFirst()
        val photoPath = cursor?.getString(0)
        val file = File(photoPath)
        urlImage = file.toString()
        return Uri.parse(path)
    }

    private fun processCapturedPhoto() {
        val cursor = requireActivity().contentResolver.query(
            Uri.parse(mCurrentPhotoPath),
            Array(1) {android.provider.MediaStore.Images.ImageColumns.DATA},
            null, null, null)
        cursor?.moveToFirst()
        val photoPath = cursor?.getString(0)
        cursor?.close()
        val file = File(photoPath)
        val uri = Uri.fromFile(file)

        val height =200
        val width = 300

        val request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setResizeOptions(ResizeOptions(width, height))
            .build()
        val controller = Fresco.newDraweeControllerBuilder()
            .setOldController(binding.checkListPhoto?.controller)
            .setImageRequest(request)
            .build()
        binding.checkListPhoto?.controller = controller
        Toast.makeText(context, file.toString(), Toast.LENGTH_SHORT).show()
    }


}