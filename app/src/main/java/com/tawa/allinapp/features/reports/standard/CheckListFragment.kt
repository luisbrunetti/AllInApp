package com.tawa.allinapp.features.reports.standard

import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import android.os.Environment
import android.graphics.BitmapFactory
import android.text.InputType
import android.util.LayoutDirection
import android.util.Log
import androidx.compose.animation.slideIn
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.databinding.HeaderReportBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import android.widget.Spinner
import android.R.attr.data
import androidx.core.net.toFile
import android.R.attr.data
import android.content.*
import android.database.Cursor
import android.provider.DocumentsContract
import android.util.Base64
import java.io.*


class CheckListFragment: BaseFragment() {

    private lateinit var binding: FragmentChecklistBinding
    private lateinit var headerReportBinding: HeaderReportBinding
    private val TAKE_PHOTO_REQUEST = 101
    var photoFile: File? = null
    val CAPTURE_IMAGE_REQUEST = 1
    var mCurrentPhotoPath: String? = null
    private lateinit var checkListViewModel: CheckListViewModel
    private lateinit var  listInit:List<Answer>
    private lateinit var  listFilter:List<Answer>
    private var state:Boolean = false
    private var verify:Boolean = false
    private  var idPhoto = ""
    private var idQuestion = ""
    private var idAnswerGen = ""
    private var idReport=""
    private var typeReport = ""
    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val params1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val params2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    var urlImage = ""
    private  var statePhoto = 0
    private var  listRadioButton = ArrayList<RadioButton>()
    private var  listCheckBox = ArrayList<CheckBox>()
    private var  listInput = ArrayList<EditText>()
    private var  listCheckBoxBd = ArrayList<CheckBox>()
    private var  listRadioButtonBd = ArrayList<RadioButton>()
    private var  listSpinner = ArrayList<Spinner>()
    private var  listSpinnerBd = ArrayList<Spinner>()
    private var  listInputNumber = ArrayList<EditText>()
    private var  listInputDate = ArrayList<EditText>()
    private var  listInputTime= ArrayList<EditText>()
    private var  listImages= ArrayList<ImageView>()
    private var  listViews = ArrayList<View>()
    private var  listImagesSelect= ArrayList<ImageView>()
    private var  listViewsSelect = ArrayList<View>()
    private val arrayId = mutableMapOf<String,ArrayList<String>>()
    private val arrayIdQuestion =  mutableMapOf<String,ArrayList<String>>()
    private val arrayNameQ = mutableMapOf<String,ArrayList<String>>()
    private val arrayIdBd = mutableMapOf<String,ArrayList<String>>()
    private val arrayIdQuestionBd =  mutableMapOf<String,ArrayList<String>>()
    private val arrayNameQBd = mutableMapOf<String,ArrayList<String>>()
    val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChecklistBinding.inflate(inflater)
        checkListViewModel = viewModel(viewModelFactory) {
            observe(questions, { it?.let {
                listInit = it
                listFilter = it.distinctBy { it.nameQuestion }
                for(list in listFilter){
                    val listAnswer = listInit.filter { it.nameQuestion==list.nameQuestion }
                    when(list.objectType)
                    {
                        "Caja de texto" -> addAnswersInput(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "Check" -> addAnswersCheck(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "Option" ->  addAnswersRadio(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "BD - Check" -> addAnswersCheckBd(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "BD - Option" -> addAnswersRadioBd(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "Lista desplegable" -> addAnswersSpinner(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "BD - Lista desplegable" -> addAnswersSpinnerBd(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "Numérico" -> addAnswersInputNumber(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "Fecha" -> addAnswersDate(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "Hora" -> addAnswersTime(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "Imagen de cámara" -> addAnswersPhoto(listAnswer,binding.contentCheckList,list.nameQuestion)
                        "Imagen de biblioteca" -> addAnswersSelectImage(listAnswer,binding.contentCheckList,list.nameQuestion)
                        //"Cuadrícula de varias opciones" -> addAnswersMultiRadio(listAnswer,binding.contentCheckList,list.nameQuestion)
                    }
                        //addAnswersRadio(listAnswer,binding.contentCheckList,list.nameQuestion)
                }
                  // showQuestions(list.objectType,list.id,list.questionName,list.order)
            }})
            observe(stateReport,{it?.let {
                getLastLocation()
                if(it.isNotEmpty())
                {
                    typeReport=it
                    checkListViewModel.getQuestions(idReport)
                }
            } })
            observe(answersGen, { it?.let {
                if(orderGen.value!! >0)
                    Log.d("respueastas",it.toString())
                    //addAnswersRadio(it,binding.contentCheckList,nameQuestion.value!!,orderRadio.value!!)
            } })
            observe(successSyncReportStandard,{it?.let {
                    if(it)
                        activity?.onBackPressed()
            } })
            observe(answersRadio, { it?.let {
             //   if(orderRadio.value!! >0)
               //     addAnswersRadio(it,binding.contentCheckList,nameQuestion.value!!,orderRadio.value!!)
            } })

            observe(answersCheck, { it?.let {
               // if(orderCheckBox.value!! >0)
                  //  addAnswersCheck(it,binding.contentCheckList,nameQuestion.value!!,orderCheckBox.value!!)
            } })
            observe(answersInput, { it?.let {
                //if(orderInput.value!! >0)
                 //   addAnswersInput(it,binding.contentCheckList,nameQuestion.value!!,orderInput.value!!)

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
                checkListViewModel.updateReportPv(idReport,"En proceso","Terminado",Calendar.getInstance().toInstant().toString(),latitude,longitude)
                for (radio in listRadioButton) {
                    val tag = radio.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],radio.isChecked.toString(),urlImage)
                    // checkListViewModel.updateAnswers(tag[0], radio.isChecked.toString())
                }
                for (radio in listRadioButtonBd) {
                    val tag = radio.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],radio.isChecked.toString(),urlImage)
                    // checkListViewModel.updateAnswers(tag[0], radio.isChecked.toString())
                }
                for (check in listCheckBox) {
                    val tag = check.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],check.isChecked.toString(),urlImage)
                    // checkListViewModel.updateAnswers(tag[0], check.isChecked.toString())
                }
                for (check in listCheckBoxBd) {
                    val tag = check.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],check.isChecked.toString(),urlImage)
                    // checkListViewModel.updateAnswers(tag[0], check.isChecked.toString())
                }
                for (input in listInput) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                for (input in listInputNumber) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                for (input in listInputTime) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                for (input in listInputDate) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                for (input in listSpinner) {
                    val index = input.selectedItemPosition
                    if(index>0)
                    {
                        val tag = input.tag
                        val data = arrayId[tag]
                        val dataName = arrayNameQ[tag]
                        checkListViewModel.setAnswerPv(data?.get(index)!!,tag.toString(),input.selectedItem.toString(),urlImage)
                    }

                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listSpinnerBd) {
                    val index = input.selectedItemPosition
                    if(index>0)
                    {
                        val tag = input.tag
                        val data = arrayIdBd[tag]
                        val dataName = arrayNameQBd[tag]
                        checkListViewModel.setAnswerPv(data?.get(index)!!,tag.toString(),input.selectedItem.toString(),urlImage)
                        //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                    }

                }
                for (input in listImages) {
                    val tag = input.tag as ArrayList<String>
                    if(tag[2]!="VACIO")
                        checkListViewModel.setAnswerPv(tag[0],tag[1],tag[2],urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listImagesSelect) {
                    val tag = input.tag as ArrayList<String>
                    if(tag[2]!="VACIO")
                        checkListViewModel.setAnswerPv(tag[0],tag[1],tag[2],urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                //checkListViewModel.setAnswerPv(idPhoto,idQuestion,urlImage,urlImage)
                //checkListViewModel.updateAnswers(idPhoto, urlImage)

                findElements()

                showConfirmSync()
            }
            else
                notify(activity, R.string.register_ready)
        }
        binding.btnBackCheckList.setOnClickListener{
            activity?.onBackPressed()
        }

        binding.btnBr.setOnClickListener {
            if(typeReport!="Terminado") {
                //checkListViewModel.updateState(true, false)
                checkListViewModel.updateReportPv(idReport,"En proceso","Borrador","","","")
                for (radio in listRadioButton) {
                    val tag = radio.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],radio.isChecked.toString(),urlImage)
                   // checkListViewModel.updateAnswers(tag[0], radio.isChecked.toString())
                }
                for (radio in listRadioButtonBd) {
                    val tag = radio.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],radio.isChecked.toString(),urlImage)
                    // checkListViewModel.updateAnswers(tag[0], radio.isChecked.toString())
                }
                for (check in listCheckBox) {
                    val tag = check.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],check.isChecked.toString(),urlImage)
                   // checkListViewModel.updateAnswers(tag[0], check.isChecked.toString())
                }
                for (check in listCheckBoxBd) {
                    val tag = check.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],check.isChecked.toString(),urlImage)
                    // checkListViewModel.updateAnswers(tag[0], check.isChecked.toString())
                }
                for (input in listInput) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                for (input in listInputNumber) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                for (input in listInputTime) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                for (input in listInputDate) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }

                for (input in listSpinner) {
                    val index = input.selectedItemPosition
                    if(index>0)
                    {
                        val tag = input.tag
                        val data = arrayId[tag]
                        val dataName = arrayNameQ[tag]
                        checkListViewModel.setAnswerPv(data?.get(index)!!,tag.toString(),input.selectedItem.toString(),urlImage)
                    }

                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listSpinnerBd) {
                    val index = input.selectedItemPosition
                    if(index>0)
                    {
                        val tag = input.tag
                        val data = arrayIdBd[tag]
                        val dataName = arrayNameQBd[tag]
                        checkListViewModel.setAnswerPv(data?.get(index)!!,tag.toString(),input.selectedItem.toString(),urlImage)
                        //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                    }

                }
                for (input in listImages) {
                    val tag = input.tag as ArrayList<String>
                    if(tag[2]!="VACIO")
                        checkListViewModel.setAnswerPv(tag[0],tag[1],tag[2],urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listImagesSelect) {
                    val tag = input.tag as ArrayList<String>
                    if(tag[2]!="VACIO")
                        checkListViewModel.setAnswerPv(tag[0],tag[1],tag[2],urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                //checkListViewModel.updateAnswers(idPhoto, urlImage)
               // checkListViewModel.setAnswerPv(idPhoto,idQuestion,urlImage,urlImage)
                activity?.onBackPressed()
            }
            else
                notify(activity, R.string.register_ready)
        }
        return binding.root
    }

    private fun findElements(){
       /* checkListViewModel.startRadio()
        checkListViewModel.startCheck()
        checkListViewModel.startInput()*/

        for (radio in listRadioButton) {
            val tag = radio.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],radio.isChecked.toString(),urlImage)
        }
        for (radio in listRadioButtonBd) {
            val tag = radio.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],radio.isChecked.toString(),urlImage)
            // checkListViewModel.updateAnswers(tag[0], radio.isChecked.toString())
        }
        for (check in listCheckBox) {
            val tag = check.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],check.isChecked.toString(),urlImage)
        }
        for (check in listCheckBoxBd) {
            val tag = check.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],check.isChecked.toString(),urlImage)
            // checkListViewModel.updateAnswers(tag[0], check.isChecked.toString())
        }
        for (input in listInput) {
            val tag = input.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.text.toString(),urlImage)
            //checkListViewModel.updateAnswers(tag[0], input.text.toString())
        }

        for (input in listInputNumber) {
            val tag = input.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.text.toString(),urlImage)
            //checkListViewModel.updateAnswers(tag[0], input.text.toString())
        }

        for (input in listInputTime) {
            val tag = input.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.text.toString(),urlImage)
            //checkListViewModel.updateAnswers(tag[0], input.text.toString())
        }

        for (input in listInputDate) {
            val tag = input.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.text.toString(),urlImage)
            //checkListViewModel.updateAnswers(tag[0], input.text.toString())
        }

        for (input in listSpinner) {
            val index = input.selectedItemPosition
            if(index>0)
            {
                val tag = input.tag
                val data = arrayId[tag]
                val dataName = arrayNameQ[tag]
                checkListViewModel.setReadyAnswers(tag.toString(),dataName?.get(index)!!,data?.get(index)!!,input.selectedItem.toString(),urlImage)
            }

            //checkListViewModel.updateAnswers(tag[0], input.text.toString())
        }
        for (input in listSpinnerBd) {
            val index = input.selectedItemPosition
            if(index>0)
            {
                val tag = input.tag
                val data = arrayIdBd[tag]
                val dataName = arrayNameQBd[tag]
                checkListViewModel.setReadyAnswers(tag.toString(),dataName?.get(index)!!,data?.get(index)!!,input.selectedItem.toString(),urlImage)
            }

        }
        for (input in listImages) {
            val tag = input.tag as ArrayList<String>
            if(tag[2]!="VACIO")
                checkListViewModel.setReadyAnswers(tag[1],tag[3],tag[0],tag[2],urlImage)
            //checkListViewModel.updateAnswers(tag[0], input.text.toString())
        }
        for (input in listImagesSelect) {
            val tag = input.tag as ArrayList<String>
            if(tag[2]!="VACIO")
                checkListViewModel.setReadyAnswers(tag[1],tag[3],tag[0],tag[2],urlImage)
            //checkListViewModel.updateAnswers(tag[0], input.text.toString())
        }
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
                checkListViewModel.getAnswersGen(id,questionName,order)
               /* if(type=="CHECK")
                    checkListViewModel.getAnswersRadio(id,questionName,order)
                if(type=="CHECK LIST")
                    checkListViewModel.getAnswersCheck(id,questionName,order)
                if(type=="INPUT")
                    checkListViewModel.getAnswersInput(id,questionName,order)
                if(type=="FOTO")
                    checkListViewModel.getAnswersPhoto(id,questionName)*/
    }

    private fun addAnswersSpinner(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String){
        params1.setMargins(0,0, 0, 0)
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)

        val radioGroup = RadioGroup(context)
        val spinner = Spinner(context)
        spinner.setPadding(10f.toDips().toInt(),0,0,0)
        val arrayAnswer = arrayListOf<String>()
        arrayAnswer.add("Seleccione una opción")
        arrayId.getOrPut(listAnswers[0].idQuestion){ arrayListOf()}.add("")
        arrayIdQuestion.getOrPut(listAnswers[0].idQuestion){ arrayListOf()}.add("")
        arrayNameQ.getOrPut(listAnswers[0].idQuestion){ arrayListOf()}.add("")
        listAnswers.forEach {
            arrayAnswer.add(it.answerName)
            arrayId.getOrPut(it.idQuestion){ arrayListOf()}.add(it.id)
            arrayIdQuestion.getOrPut(it.idQuestion){ arrayListOf()}.add(it.idQuestion)
            arrayNameQ.getOrPut(it.idQuestion){ arrayListOf()}.add(it.nameQuestion)
        }
        spinner.setBackgroundResource(R.drawable.spinner_reporteador)
        val aa = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,arrayAnswer)
        spinner.adapter = aa
        spinner.tag = listAnswers[0].idQuestion
        val dataSpinner = listAnswers.filter { it.data!="false" }
        if(typeReport!="0"){
            if(dataSpinner.isNotEmpty())
                spinner.setSelection(getIndex(spinner,dataSpinner[0].data))
        }
        listSpinner.add(spinner)
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
        linearL.addView(spinner)
        params.setMargins(0,0, 0, 0)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersSpinnerBd(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String){
        params1.setMargins(0,0, 0, 0)
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)

        val radioGroup = RadioGroup(context)
        val spinner = Spinner(context)
        spinner.setPadding(10f.toDips().toInt(),0,0,0)
        val arrayAnswer = arrayListOf<String>()
        arrayAnswer.add("Seleccione una opción")
        arrayIdBd.getOrPut(listAnswers[0].idQuestion){ arrayListOf()}.add("")
        arrayIdQuestionBd.getOrPut(listAnswers[0].idQuestion){ arrayListOf()}.add("")
        arrayNameQBd.getOrPut(listAnswers[0].idQuestion){ arrayListOf()}.add("")
        listAnswers.forEach {
            arrayAnswer.add(it.answerName)
            arrayIdBd.getOrPut(it.idQuestion){ arrayListOf()}.add(it.id)
            arrayIdQuestionBd.getOrPut(it.idQuestion){ arrayListOf()}.add(it.idQuestion)
            arrayNameQBd.getOrPut(it.idQuestion){ arrayListOf()}.add(it.nameQuestion)
        }
        spinner.setBackgroundResource(R.drawable.spinner_reporteador)
        val aa = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,arrayAnswer)

        spinner.adapter = aa
        spinner.tag = listAnswers[0].idQuestion
        val dataSpinner = listAnswers.filter { it.data!="false" }
        if(typeReport!="0"){
            if(dataSpinner.isNotEmpty())
                spinner.setSelection(getIndex(spinner,dataSpinner[0].data))
        }
        listSpinnerBd.add(spinner)
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
        linearL.addView(spinner)
        params.setMargins(0,0, 0, 0)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersRadio(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String){
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
           linear.addView(linearL)

    }

    private fun addAnswersRadioBd(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String){
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
            listRadioButtonBd.add(radioButton)
            radioGroup.addView(radioButton)
        }
        linearL.addView(radioGroup)
        linearL.layoutParams = params
        linear.addView(linearL)

    }


    private fun addAnswersMultiRadio(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String){
        params.setMargins(0,0, 0, 10f.toDips().toInt())
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)
        val linearColumn = LinearLayout(context)
        linearColumn.orientation = LinearLayout.HORIZONTAL

        val radioGroup = RadioGroup(context)
        val listColumns  = arrayListOf<String>()
        val listRows  = arrayListOf<String>()
        val listId  = arrayListOf<String>()
        val listIdQuestion = arrayListOf<String>()
        val listNameQ = arrayListOf<String>()
        val listData = arrayListOf<Boolean>()
        for(list in listAnswers)
        {
            if(list.column)
            {
                /*val textColumn = TextView(context)
                textColumn.text = list.answerName
                linearColumn.addView(textColumn)*/
                listColumns.add(list.answerName)
            }
            if(list.row)
            {
                listRows.add(list.answerName)
                listId.add(list.id)
                listIdQuestion.add(list.idQuestion)
                listNameQ.add(nameQ)
                listData.add(list.data.toBoolean())

            }
                /*val radioButton  = RadioButton(context)
                val listTag =ArrayList<String>(2)
                radioButton.text = list.answerName
                radioButton.layoutDirection = View.LAYOUT_DIRECTION_RTL
                listTag.add(list.id)
                listTag.add(list.idQuestion)
                listTag.add(nameQ)
                radioButton.tag = listTag
                if(typeReport!="0")
                    radioButton.isChecked = list.data.toBoolean()
                listRadioButton.add(radioButton)
                radioGroup.addView(radioButton)
            }*/
           // listColumns.add(list.)

        }
        var count = 0
        listColumns.forEach {
            val linearRow = LinearLayout(context)
            linearRow.orientation = LinearLayout.VERTICAL
            val textColumn = TextView(context)
            textColumn.text = it
           // linearColumn.addView(textColumn)
            val rg = RadioGroup(context)
            listRows.forEach { row->
                val radioButton  = RadioButton(context)
                if(count==0)
                    radioButton.text = row
                val listTag =ArrayList<String>(2)
                radioButton.layoutDirection = View.LAYOUT_DIRECTION_RTL
                rg.addView(radioButton)
              //  linearRow.addView(radioButton)
            }
            linearRow.addView(textColumn)
            linearRow.addView(rg)
            linearColumn.addView(linearRow)
            count++
        }
        linearL.addView(linearColumn)
       // linearL.addView(radioGroup)
        linearL.layoutParams = params
        linear.addView(linearL)
    }

    private fun addAnswersCheck(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String){
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
            linear.addView(linearL)

    }

    private fun addAnswersCheckBd(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String){
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

            listCheckBoxBd.add(checkBox)
            linearL.addView(checkBox)
        }
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersInput(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.layoutParams = params1
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
        linear.addView(linearL)

    }

    private fun addAnswersInputNumber(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)

        for(list in listAnswers)
        {
            val editText  = EditText(context)
            editText.setBackgroundResource(R.drawable.rounded)
            editText.inputType = InputType.TYPE_CLASS_NUMBER
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
            listInputNumber.add(editText)
            linearL.addView(editText)
        }
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersDate(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)

        for(list in listAnswers)
        {
            val editText  = EditText(context)
            editText.setBackgroundResource(R.drawable.rounded)
            editText.isFocusable = false
           // editText.isEnabled = false
            editText.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_calendar_home,
                0
            )
            editText.setOnClickListener {
                getDay(editText)
            }
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
            listInputDate.add(editText)
            linearL.addView(editText)
        }
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersTime(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)

        for(list in listAnswers)
        {
            val editText  = EditText(context)
            editText.setBackgroundResource(R.drawable.rounded)
            editText.isFocusable = false
            // editText.isEnabled = false
            /*editText.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_calendar_home,
                0
            )*/
            editText.setOnClickListener {
                getTime(editText)
            }
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
            listInputTime.add(editText)
            linearL.addView(editText)
        }
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersPhoto(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)

        for(list in listAnswers)
        {
            val editText  = Button(context)
            editText.setBackgroundResource(R.drawable.bg_blue_button_reports)
            editText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            editText.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_report_photo,
                0,
               0,
                0
            )
            editText.setPadding((16f).toDips().toInt(),0,(16f).toDips().toInt(),0)
            editText.height = (50f).toDips().toInt()
            val listTag =ArrayList<String>(2)
            listTag.add(list.id)
            listTag.add(list.idQuestion)
            listTag.add(nameQ)
            editText.tag = listTag
            editText.text =   "Tomar Foto"
            val view =layoutInflater.inflate(R.layout.photo_report,null)
            params2.topMargin = 30
            params2.height = 200f.toDips().toInt()
            view.layoutParams = params2
            val image =view.findViewById<ImageView>(R.id.ivPictureReport)
            val imageClose =view.findViewById<ImageView>(R.id.ivCloseReport)
            view.isVisible = false
            val listTag1 =ArrayList<String>(2)
            listTag1.add(list.id)
            listTag1.add(list.idQuestion)
            listTag1.add("VACIO")
            listTag1.add(nameQ)
            image.setImageResource(R.drawable.ic_img)
            image.tag = listTag1
            if(typeReport!="0") {
                if(list.data.isNotEmpty())
                {
                    view.isVisible = true
                    val uriImage = Uri.fromFile(File(list.data))
                    image.setImageURI(uriImage)
                    val tag = image.tag as ArrayList<String>
                    tag[2]=list.data
                }
            }
            editText.setOnClickListener {
                idAnswerGen = list.id
                validatePermissions()
            }
            imageClose.setOnClickListener {
                val tag = image.tag as ArrayList<String>
                tag[2]=""
                view.isVisible = false
            }
            //listInput.add(editText)
            linearL.addView(editText)
            listImages.add(image)
            listViews.add(view)
            linearL.addView(view)
        }
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersSelectImage(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = nameQ
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)

        for(list in listAnswers)
        {
            val editText  = Button(context)
            editText.setBackgroundResource(R.drawable.bg_blue_button_reports)
            editText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            editText.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_image_report,
                0,
                0,
                0
            )
            editText.setPadding((16f).toDips().toInt(),0,(16f).toDips().toInt(),0)
            editText.height = (50f).toDips().toInt()
            val listTag =ArrayList<String>(2)
            listTag.add(list.id)
            listTag.add(list.idQuestion)
            listTag.add(nameQ)
            editText.tag = listTag
            editText.text =   "Seleccionar archivo"
            val view =layoutInflater.inflate(R.layout.photo_report,null)
            params2.topMargin = 30
            params2.height = 200f.toDips().toInt()
            view.layoutParams = params2
            val image =view.findViewById<ImageView>(R.id.ivPictureReport)
            val imageClose =view.findViewById<ImageView>(R.id.ivCloseReport)
            view.isVisible = false
            val listTag1 =ArrayList<String>(2)
            listTag1.add(list.id)
            listTag1.add(list.idQuestion)
            listTag1.add("VACIO")
            listTag1.add(nameQ)
            image.setImageResource(R.drawable.ic_img)
            image.tag = listTag1
            if(typeReport!="0") {
                if(list.data.isNotEmpty())
                {
                    view.isVisible = true
                    val uriImage = Uri.fromFile(File(list.data))
                    image.setImageURI(uriImage)
                    val tag = image.tag as ArrayList<String>
                    tag[2]=list.data
                }
            }
            editText.setOnClickListener {
                idAnswerGen = list.id
                imagePicker()
            }
            imageClose.setOnClickListener {
                val tag = image.tag as ArrayList<String>
                tag[2]=""
                view.isVisible = false
            }
            //listInput.add(editText)
            linearL.addView(editText)
            listImagesSelect.add(image)
            listViewsSelect.add(view)
            linearL.addView(view)
        }
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    private fun imagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        try {
            startActivityForResult(intent, REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace();
            Log.e("tag", "error")
        }

    }

    private fun getDay(et: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), { _, _, monthOfYear, dayOfMonth ->

            et.setText("" + dayOfMonth + "/" + getMonth(monthOfYear) + "/" + year)
        }, year, month, day)
        dpd.show()
    }
    private fun getTime(et: EditText){
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            et.setText( SimpleDateFormat("HH:mm aa").format(cal.time))
        }
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }

    private fun convertBase64(path: String): String? {
        val imageFile = File(path)
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(imageFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bm = BitmapFactory.decodeStream(fis)
        val outputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun getMonth(monthYear: Int) = when(monthYear){
        0 -> "01"
        1 -> "02"
        2 -> "03"
        3 -> "04"
        4 -> "05"
        5 -> "06"
        6 -> "07"
        7 -> "08"
        8 -> "09"
        9 -> "10"
        10 -> "11"
        11 -> "12"
        else  ->""
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
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            urlImage = photoFile!!.absolutePath
            /*binding.checkListPhoto.isVisible = true
            binding.deletePhoto.isVisible = true
            urlImage = photoFile!!.absolutePath
            val imageUri = Uri.fromFile(File(urlImage))
            binding.checkListPhoto.setImageURI(imageUri,"")
            statePhoto = 1*/
            for((index,image) in listImages.withIndex())
            {
                val tag = image.tag as ArrayList<String>
                if(tag[0]==idAnswerGen)
                {
                    tag[2] = urlImage
                    image.isVisible  = true
                    listViews[index].isVisible = true
                    val imageUri = Uri.fromFile(File(urlImage))
                    image.setImageURI(imageUri)
                }
            }
        }
        if(requestCode == REQUEST_CODE&&resultCode==Activity.RESULT_OK)
        {
            val realPath  = getRealPathFromURI(requireContext(),data?.data!!)
            for((index,image) in listImagesSelect.withIndex())
            {
                val tag = image.tag as ArrayList<String>
                if(tag[0]==idAnswerGen)
                {
                    tag[2] = realPath.toString()
                    image.isVisible  = true
                    listViewsSelect[index].isVisible = true
                    val imageUri = Uri.fromFile(File(realPath))
                    image.setImageURI(imageUri)
                }
            }

        }
    }

    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                            }
                            // This is for checking SD Card
                        } else {
                            "storage" + "/" + docId.replace(":", "/")
                        }
                    }
                    isDownloadsDocument(uri) -> {
                        val fileName = getFilePath(context, uri)
                        if (fileName != null) {
                            return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                        }
                        var id = DocumentsContract.getDocumentId(uri)
                        if (id.startsWith("raw:")) {
                            id = id.replaceFirst("raw:".toRegex(), "")
                            val file = File(id)
                            if (file.exists()) return id
                        }
                        val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                        return getDataColumn(context, contentUri, null, null)
                    }
                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                }
            }
            "content".equals(uri.scheme, ignoreCase = true) -> {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
            }
            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }
        return null
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(uri, projection, null, null,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }


}