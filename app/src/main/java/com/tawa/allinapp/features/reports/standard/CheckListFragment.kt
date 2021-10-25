package com.tawa.allinapp.features.reports.standard

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.InputType
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.ConditionalDialogFragment
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.*
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentChecklistBinding
import com.tawa.allinapp.databinding.HeaderReportBinding
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.RecordAudioViews
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
    val paramsMulti = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
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
    private var  listMultiRadio = ArrayList<RadioButton>()
    private var  listMultiCheck = ArrayList<CheckBox>()
    private var  listMultiText = ArrayList<EditText>()
    private var  listColumnsRadio = ArrayList<TextView>()
    private var  listRowsRadio = ArrayList<TextView>()
    private var  listRowsCheck = ArrayList<TextView>()
    private var  listColumnsCheck = ArrayList<TextView>()
    private var  listColumnsInput = ArrayList<TextView>()
    private var  listRowsInput = ArrayList<TextView>()
    private var listErrorText = ArrayList<TextView>()
    private var listErrorRadio = ArrayList<TextView>()
    private var listErrorCheck = ArrayList<TextView>()
    private var listErrorCheckBd = ArrayList<TextView>()
    private var listErrorRadioBd = ArrayList<TextView>()
    private var listErrorList = ArrayList<TextView>()
    private var listErrorListBd = ArrayList<TextView>()
    private var listErrorNumber = ArrayList<TextView>()
    private var listErrorDate = ArrayList<TextView>()
    private var listErrorTime = ArrayList<TextView>()
    private var listErrorPhoto = ArrayList<TextView>()
    private var listErrorImageSelect = ArrayList<TextView>()
    private var listErrorMultiRadio = ArrayList<TextView>()
    private var listErrorMultiCheck = ArrayList<TextView>()
    private var listErrorMultiInput = ArrayList<TextView>()
    private var listErrorAudio = ArrayList<TextView>()
    private val listFlag = ArrayList<Int>()
    private val arrayId = mutableMapOf<String,ArrayList<String>>()
    private val arrayIdQuestion =  mutableMapOf<String,ArrayList<String>>()
    private val arrayNameQ = mutableMapOf<String,ArrayList<String>>()
    private val arrayIdBd = mutableMapOf<String,ArrayList<String>>()
    private val arrayIdQuestionBd =  mutableMapOf<String,ArrayList<String>>()
    private val arrayNameQBd = mutableMapOf<String,ArrayList<String>>()
    private val textError = "El campo es obligatorio"
    val REQUEST_CODE = 100
    //RECORD AUDIO
    private var audioLimit: Int = 1000 * 60 * 7 // 7 minutos
    private var mutableHashMapTag: MutableMap<String, RecordAudioViews> = mutableMapOf()
    private var hashPathAudioRecorded: MutableMap<String,String> = mutableMapOf()
    private var hashPathAudioSelected: MutableMap<String,String> = mutableMapOf()
    private var currentLayout: String = ""
    private var recording: Boolean = false
    private var errorRecording: Boolean = false
    private var recorder: MediaRecorder? = null
    private var playerRecorded: MediaPlayer? = null
    private var playerSelected: MediaPlayer? = null
    private var arrayAudioQuestions : ArrayList<String> = ArrayList()
    var recordPath: String = ""
    companion object{
        val TYPE_SELECTED = "SELECTED"
        val TYPE_RECCORDED = "RECORDED"
        val ENABLED = "ENABLED"
        val DISABLED = "DISABLED"
    }

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { uri ->
            val realPath = getRealPathFromURI(requireContext(), uri)
            realPath?.let { realPath->
                try{
                    Log.d("realPath", realPath.toString())
                    val audioSelectedName = getFilename(uri)
                    Log.d("audioSelectedNmae",audioSelectedName.toString())
                    mutableHashMapTag[currentLayout]?.let {
                        //it.textViewAudioSelected?.visibility = View.GONE
                        it.layoutShowSelectedAudio?.visibility = View.VISIBLE
                        it.textViewShowSelecetedAudio?.text = audioSelectedName
                        hashPathAudioSelected[currentLayout] = realPath
                        it.answerSelected = realPath

                    }
                    //val converted = dynamicAudioViewModel.convertAudioSelectedTo64Format(realPath.toString())
                }catch (e : Exception){
                    Log.d("exception", e.toString())
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }
    override fun changeViewsFragment() {

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
                        "Caja de texto" -> addAnswersInput(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Check" -> addAnswersCheck(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Option" ->  addAnswersRadio(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "BD - Check" -> addAnswersCheckBd(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "BD - Option" -> addAnswersRadioBd(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Lista desplegable" -> addAnswersSpinner(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "BD - Lista desplegable" -> addAnswersSpinnerBd(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Numérico" -> addAnswersInputNumber(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Fecha" -> addAnswersDate(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Hora" -> addAnswersTime(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Imagen de cámara" -> addAnswersPhoto(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Imagen de biblioteca" -> addAnswersSelectImage(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Cuadrícula de varias opciones" -> addAnswersMultiRadio(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Cuadrícula de varias opciones-check" -> addAnswersMultiCheck(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Cuadrícula de varias opciones - numérico" -> addAnswersMultiInput(listAnswer,binding.contentCheckList,list.nameQuestion,list.orderQ,list.required)
                        "Audio" -> addTwoButtonsAudio(listAnswer,binding.contentCheckList,list.nameQuestion, list.orderQ)
                    }
                }
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
            Log.d("idReport",idRep.toString())
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
            if(typeReport!="Terminado") {
                listFlag.clear()
                hideTextError()
                for(question in listFilter)
                {
                    if(question.objectType=="Caja de texto")
                    {
                        if(question.required)
                        {
                            val c = listInput.filter {
                                val tag = it.tag as ArrayList<String>
                                it.text.toString().isNotEmpty()  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorText.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Check")
                    {
                        if(question.required)
                        {
                            val c = listCheckBox.filter {
                                val tag = it.tag as ArrayList<String>
                                it.isChecked  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorCheck.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Option")
                    {
                        if(question.required)
                        {
                            val c = listRadioButton.filter {
                                val tag = it.tag as ArrayList<String>
                                it.isChecked  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorRadio.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="BD - Check")
                    {
                        if(question.required)
                        {
                            val c = listCheckBoxBd.filter {
                                val tag = it.tag as ArrayList<String>
                                it.isChecked  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorCheckBd.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="BD - Option")
                    {
                        if(question.required)
                        {
                            val c = listRadioButtonBd.filter {
                                val tag = it.tag as ArrayList<String>
                                it.isChecked  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorRadioBd.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Numérico")
                    {
                        if(question.required)
                        {
                            val c = listInputNumber.filter {
                                val tag = it.tag as ArrayList<String>
                                it.text.toString().isNotEmpty()  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorNumber.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Fecha")
                    {
                        if(question.required)
                        {
                            val c = listInputDate.filter {
                                val tag = it.tag as ArrayList<String>
                                it.text.toString().isNotEmpty()  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorDate.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Hora")
                    {
                        if(question.required)
                        {
                            val c = listInputTime.filter {
                                val tag = it.tag as ArrayList<String>
                                it.text.toString().isNotEmpty()  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorTime.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Lista desplegable")
                    {
                        if(question.required)
                        {
                            val c = listSpinner.filter {
                                it.selectedItemPosition>0  && it.tag==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorList.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="BD - Lista desplegable")
                    {
                        if(question.required)
                        {
                            val c = listSpinnerBd.filter {
                                it.selectedItemPosition>0  && it.tag ==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorListBd.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Imagen de cámara")
                    {
                        if(question.required)
                        {
                            val c = listImages.filter {
                                val tag = it.tag as ArrayList<String>
                                tag[2].count()>5  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorPhoto.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Imagen de biblioteca")
                    {
                        if(question.required)
                        {
                            val c = listImagesSelect.filter {
                                val tag = it.tag as ArrayList<String>
                                tag[2].count()>5  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorImageSelect.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Cuadrícula de varias opciones")
                    {
                        if(question.required)
                        {
                            val c = listMultiRadio.filter {
                                val tag = it.tag as ArrayList<String>
                                it.isChecked  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorMultiRadio.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Cuadrícula de varias opciones-check")
                    {
                        if(question.required)
                        {
                            val c = listMultiCheck.filter {
                                val tag = it.tag as ArrayList<String>
                                it.isChecked  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorMultiCheck.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                    if(question.objectType=="Cuadrícula de varias opciones - numérico")
                    {
                        if(question.required)
                        {
                            val c = listMultiText.filter {
                                val tag = it.tag as ArrayList<String>
                                it.text.toString().isNotEmpty()  && tag[1]==question.idQuestion
                            }.count()
                            if(c>0)
                                listFlag.add(1)
                            else {
                                val textError = listErrorMultiInput.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }
                    if(question.objectType=="Audio")
                    {
                        if(question.required)
                        {
                            var count = 0
                            for(element in mutableHashMapTag.values){
                                if((element.answerSelected != "" ||
                                            element.answerRecorded != "") && element.idQuestion == question.idQuestion){
                                    count++
                                }
                            }
                            if(count > 0){
                                listFlag.add(1)
                            }else{
                                val textError = listErrorAudio.find { it.tag==question.idQuestion }
                                textError?.isVisible = true
                                listFlag.add(0)
                            }
                        }
                        else
                            listFlag.add(1)
                    }

                }

                val countRequired = listFlag.filter { it == 0 }.count()
                if(countRequired==0)
                {
                    getLastLocation()
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
                    for (input in listMultiRadio) {
                        val tag = input.tag as ArrayList<String>
                        checkListViewModel.setAnswerPv(tag[0],tag[1],input.isChecked.toString(),urlImage)
                        //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                    }
                    for (input in listMultiCheck) {
                        val tag = input.tag as ArrayList<String>
                        checkListViewModel.setAnswerPv(tag[0],tag[1],input.isChecked.toString(),urlImage)
                        //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                    }
                    for (input in listMultiText) {
                        val tag = input.tag as ArrayList<String>
                        checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                        //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                    }
                    for (input in listColumnsRadio) {
                        val tag = input.tag as ArrayList<String>
                        checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                        //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                    }
                    for (input in listColumnsCheck) {
                        val tag = input.tag as ArrayList<String>
                        checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                        //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                    }
                    for (input in listColumnsInput) {
                        val tag = input.tag as ArrayList<String>
                        checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                        //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                    }

                    for(tag in mutableHashMapTag.values){
                        //Log.d("setReadyAnswer9764", "hashTagRecord -> ${tag.answerRecorded.toString()}  hashSelected -> ${tag.answerSelected.toString()}" )
                        if (tag.answerSelected == "") {
                            val answerBase64 = convertImageFileToBase64(File(tag.answerRecorded!!))
                            checkListViewModel.setAnswerPv(tag.idAnswer,tag.idQuestion,answerBase64 ?: "", "${TYPE_RECCORDED}&${tag.answerRecorded}")
                        } else {
                            val answerBase64 = convertImageFileToBase64(File(tag.answerSelected!!))
                            checkListViewModel.setAnswerPv(tag.idAnswer,tag.idQuestion,answerBase64 ?: "", "${TYPE_SELECTED}&${tag.answerSelected}")
                        }
                    }
                    findElements()
                    showConfirmSync()
                }
                else
                    notify(activity, R.string.notify_error_register)
            }
            else
                notify(activity, R.string.register_ready)
        }
        binding.btnBackCheckList.setOnClickListener{
            activity?.onBackPressed()
        }

        binding.btnBr.setOnClickListener {
            if(typeReport!="Terminado") {
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
                    if(tag[2]!="VACIO") {

                        checkListViewModel.setAnswerPv(tag[0], tag[1], tag[2], urlImage)
                    }
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listImagesSelect) {
                    val tag = input.tag as ArrayList<String>
                    if(tag[2]!="VACIO")
                        checkListViewModel.setAnswerPv(tag[0],tag[1],tag[2],urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listMultiRadio) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.isChecked.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listMultiCheck) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.isChecked.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listMultiText) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listColumnsRadio) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listRowsRadio) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listColumnsCheck) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listRowsCheck) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listColumnsInput) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (input in listRowsInput) {
                    val tag = input.tag as ArrayList<String>
                    checkListViewModel.setAnswerPv(tag[0],tag[1],input.text.toString(),urlImage)
                    //checkListViewModel.updateAnswers(tag[0], input.text.toString())
                }
                for (i in mutableHashMapTag.values) {
                    Log.d("i", "hashTagRecord -> ${i.answerRecorded.toString()}  hashSelected -> ${i.answerSelected.toString()}")
                    val answerbyQuestion = if (i.answerSelected == "") {
                        "${TYPE_RECCORDED}&${i.answerRecorded}"
                    } else {
                        "${TYPE_SELECTED}&${i.answerSelected}"
                    }
                    checkListViewModel.setAnswerPv(
                        i.idAnswer,
                        i.idQuestion,
                        answerbyQuestion,
                        answerbyQuestion
                    )
                }
                activity?.onBackPressed()
            }
            else
                notify(activity, R.string.register_ready)
        }
        changeViewsFragment()
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
        for (input in listMultiRadio) {
            val tag = input.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.isChecked.toString(),urlImage)
        }
        for (input in listMultiCheck) {
            val tag = input.tag as ArrayList<String>
            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.isChecked.toString(),urlImage)
        }
        for (input in listMultiText) {
            val tag = input.tag as ArrayList<String>

            checkListViewModel.setReadyAnswers(tag[1],tag[2],tag[0],input.text.toString(),urlImage)
            //checkListViewModel.updateAnswers(tag[0], input.text.toString())
        }

        for (tag in mutableHashMapTag.values) {
            //Log.d("setReadyAnswer9764", "hashTagRecord -> ${tag.answerRecorded.toString()}  hashSelected -> ${tag.answerSelected.toString()}" )
            if (tag.answerSelected == "") {
                val answerBase64 = convertImageFileToBase64(File(tag.answerRecorded!!))
                checkListViewModel.setReadyAnswers(tag.idQuestion,tag.nameQuestion,tag.idAnswer,answerBase64 ?: "", "${TYPE_RECCORDED}&${tag.answerRecorded}")
            } else {
                val answerBase64 = convertImageFileToBase64(File(tag.answerSelected!!))
                checkListViewModel.setReadyAnswers(tag.idQuestion,tag.nameQuestion,tag.idAnswer,answerBase64 ?: "", "${TYPE_SELECTED}&${tag.answerSelected}")
            }
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

    private fun addAnswersSpinner(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order:String,required:Boolean){
        params1.setMargins(0,0, 0, 0)
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
        linearL.addView(spinner)
        params.setMargins(0,0, 0, 0)
        linearL.addView(radioGroup)
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorList.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersSpinnerBd(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order:String,required:Boolean){
        params1.setMargins(0,0, 0, 0)
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
        linearL.addView(spinner)
        params.setMargins(0,0, 0, 0)
        linearL.addView(radioGroup)
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorListBd.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersRadio(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order:String,required:Boolean){
            params.setMargins(0,0, 0, 10f.toDips().toInt())
            val linearL = LinearLayout(context)
            linearL.orientation = LinearLayout.VERTICAL
            val textView  = TextView(context)
            textView.text = "$order. Pregunta: $nameQ"
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            linearL.addView(textView)

            val radioGroup = RadioGroup(context)
            for(list in listAnswers)
            {
                val radioButton  = RadioButton(context)
                val listTag =ArrayList<String>(3)
                radioButton.text = list.answerName
                listTag.add(list.id)
                listTag.add(list.idQuestion)
                listTag.add(nameQ)
                listTag.add(required.toString())
                radioButton.tag = listTag
                listRadioButton.add(radioButton)
                radioGroup.addView(radioButton)
                if(typeReport!="0")
                    radioButton.isChecked = list.data.toBoolean()
            }
            val textView1 = TextView(context)
            textView1.tag = listAnswers[0].idQuestion
            textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
            textView1.text = textError
            textView1.isVisible = false
             listErrorRadio.add(textView1)
            linearL.addView(radioGroup)
            linearL.addView(textView1)

           linearL.layoutParams = params
           linear.addView(linearL)
    }

    private fun addAnswersRadioBd(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 10f.toDips().toInt())
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
            listRadioButtonBd.add(radioButton)
            radioGroup.addView(radioButton)
            if(typeReport!="0")
                radioButton.isChecked = list.data.toBoolean()
        }
        linearL.addView(radioGroup)
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorRadioBd.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }


    private fun addAnswersMultiRadio(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 10f.toDips().toInt())
        params1.bottomMargin  = 5f.toDips().toInt()
        val scroll = HorizontalScrollView(context)
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)
        val linearColumn = LinearLayout(context)
        linearColumn.orientation = LinearLayout.VERTICAL
        val listColumns  = arrayListOf<String>()
        val listElements  = arrayListOf<String>()
        val listRows  = mutableListOf<String>()
        val listId  = arrayListOf<String>()
        val listIdQuestion = arrayListOf<String>()
        val listNameQ = arrayListOf<String>()
        val listCId  = arrayListOf<String>()
        val listCIdQuestion = arrayListOf<String>()
        val listCNameQ = arrayListOf<String>()
        val listEId  = arrayListOf<String>()
        val listEIdQuestion = arrayListOf<String>()
        val listENameQ = arrayListOf<String>()
        val listEData = arrayListOf<Boolean>()
        for(list in listAnswers.sortedBy { it.order })
        {
            if(list.column)
            {
                listColumns.add(list.answerName)
                listCId.add(list.id)
                listCIdQuestion.add(list.idQuestion)
                listCNameQ.add(nameQ)
            }
            if(list.row)
            {
                listRows.add(list.answerName)
                listId.add(list.id)
                listIdQuestion.add(list.idQuestion)
                listNameQ.add(nameQ)
            }
            if(!list.row && !list.column)
            {
                listElements.add(list.answerName)
                listEId.add(list.id)
                listEIdQuestion.add(list.idQuestion)
                listENameQ.add(nameQ)
                listEData.add(list.data.toBoolean())
            }
        }
        val countRow  = listRows.distinct().count()+1
        val countColumn  = listColumns.count()
        var count = 0
        var countRadio = 0
        for(i in 0 until countRow)
        {

            if(i==0)
            {
                val linearRow = LinearLayout(context)
                linearRow.orientation = LinearLayout.HORIZONTAL
                var countC = 0
                val textColumn = TextView(context)
                val params5 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                textColumn.width = 80f.toDips().toInt()
                textColumn.textSize = 16f
                textColumn.setTextColor(resources.getColor(R.color.colorTextAll))
                textColumn.height = 80f.toDips().toInt()
                textColumn.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                textColumn.layoutParams = params5
                textColumn.text = ""
                linearRow.addView(textColumn)
                listColumns.forEach {
                    val textColumn = TextView(context)
                    textColumn.width = 100f.toDips().toInt()
                    textColumn.textSize = 16f
                    textColumn.setTextColor(resources.getColor(R.color.colorTextAll))
                    textColumn.height = 80f.toDips().toInt()
                    textColumn.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    textColumn.setPadding(0,0,5f.toDips().toInt(),0)
                    textColumn.layoutParams = params5
                    textColumn.text = it
                    val listTag =ArrayList<String>(2)
                    listTag.add(listCId[countC])
                    listTag.add(listCIdQuestion[countC])
                    listTag.add(listCNameQ[countC])
                    textColumn.tag = listTag
                    listColumnsRadio.add(textColumn)
                    linearRow.addView(textColumn)
                    countC++
                }
                linearColumn.addView(linearRow)
            }
            else{

                val linearRow = LinearLayout(context)
                linearRow.orientation = LinearLayout.HORIZONTAL
                val rg = RadioGroup(context)
                rg.orientation = RadioGroup.HORIZONTAL

                for(j in 0 until countColumn)
                {
                    val radioButton  = RadioButton(context)
                    radioButton.width= 100f.toDips().toInt()
                    radioButton.height = 50f.toDips().toInt()
                    radioButton.textSize = 16f
                    radioButton.gravity = Gravity.START or Gravity.CENTER_VERTICAL

                    if(j==0)
                    {
                        val params4 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        val text = TextView(context)
                        text.setTextColor(resources.getColor(R.color.colorTextAll))
                        text.textSize = 16f
                        params4.rightMargin = 1f.toDips().toInt()
                        params4.topMargin = 10f.toDips().toInt()
                        params4.width = 80f.toDips().toInt()
                        //params4.height = 20f.toDips().toInt()
                        text.layoutParams = params4
                        // text.setPadding(0,(30f).toDips().toInt(),(10f).toDips().toInt(),0)
                        text.gravity = Gravity.CENTER_VERTICAL
                        text.text = listRows[count]
                        val listTag =ArrayList<String>(2)
                        listTag.add(listId[count])
                        listTag.add(listIdQuestion[count])
                        listTag.add(listNameQ[count])
                        text.tag = listTag
                        listRowsRadio.add(text)
                        linearRow.addView(text)
                    }
                        //radioButton.text = listRows[count]
                    radioButton.layoutDirection = View.LAYOUT_DIRECTION_RTL
                   val listTag =ArrayList<String>(2)
                    listTag.add(listEId[countRadio])
                    listTag.add(listEIdQuestion[countRadio])
                    listTag.add(listENameQ[countRadio])
                    radioButton.tag = listTag
                    listMultiRadio.add(radioButton)
                    rg.addView(radioButton)
                    if(typeReport!="0")
                        radioButton.isChecked =  listEData[countRadio]
                    countRadio++
                }
                count++
                linearRow.addView(rg)
                linearColumn.addView(linearRow)
            }
        }
        scroll.layoutParams = paramsMulti
        scroll.addView(linearColumn)
        linearL.addView(scroll)
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorMultiRadio.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        //scroll.addView(linearL)
        linear.addView(linearL)
    }

    private fun addAnswersMultiCheck(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 10f.toDips().toInt())
        params1.bottomMargin  = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        val scroll = HorizontalScrollView(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)
        val linearColumn = LinearLayout(context)
        linearColumn.orientation = LinearLayout.VERTICAL
        val listColumns  = arrayListOf<String>()
        val listRows  = mutableListOf<String>()
        val listElements = arrayListOf<String>()
        val listId  = arrayListOf<String>()
        val listIdQuestion = arrayListOf<String>()
        val listNameQ = arrayListOf<String>()
        val listData = arrayListOf<Boolean>()
        val listCId  = arrayListOf<String>()
        val listCIdQuestion = arrayListOf<String>()
        val listCNameQ = arrayListOf<String>()
        val listEId  = arrayListOf<String>()
        val listEIdQuestion = arrayListOf<String>()
        val listENameQ = arrayListOf<String>()
        val listEData = arrayListOf<Boolean>()
        for(list in listAnswers.sortedBy { it.order })
        {
            if(list.column)
            {
                listColumns.add(list.answerName)
                listCId.add(list.id)
                listCIdQuestion.add(list.idQuestion)
                listCNameQ.add(nameQ)
            }
            if(list.row)
            {
                listRows.add(list.answerName)
                listId.add(list.id)
                listIdQuestion.add(list.idQuestion)
                listNameQ.add(nameQ)
                listData.add(list.data.toBoolean())
            }
            if(!list.row&&!list.column)
            {
                listElements.add(list.answerName)
                listEId.add(list.id)
                listEIdQuestion.add(list.idQuestion)
                listENameQ.add(nameQ)
                listEData.add(list.data.toBoolean())
            }
        }
        val countRow  = listRows.distinct().count()+1
        val countColumn  = listColumns.count()
        var count = 0
        var countCheck = 0
        for(i in 0 until countRow)
        {
            if(i==0)
            {
                val linearRow = LinearLayout(context)
                linearRow.orientation = LinearLayout.HORIZONTAL
                var countC = 0
                val params5 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                val textColumn = TextView(context)
                params5.rightMargin = 1f.toDips().toInt()
                textColumn.width = 80f.toDips().toInt()
                textColumn.textSize = 16f
                textColumn.setTextColor(resources.getColor(R.color.colorTextAll))
                textColumn.height = 80f.toDips().toInt()
                textColumn.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                textColumn.layoutParams = params5
                textColumn.text = ""
                linearRow.addView(textColumn)
                listColumns.forEach {
                    val textColumn = TextView(context)
                    textColumn.width = 100f.toDips().toInt()
                    textColumn.textSize = 16f
                    textColumn.setTextColor(resources.getColor(R.color.colorTextAll))
                    textColumn.height = 80f.toDips().toInt()
                    textColumn.setPadding(0,0,5f.toDips().toInt(),0)
                    textColumn.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    val params4 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    textColumn.text = it
                    val listTag =ArrayList<String>(2)
                    listTag.add(listCId[countC])
                    listTag.add(listCIdQuestion[countC])
                    listTag.add(listCNameQ[countC])
                    textColumn.tag = listTag
                    listColumnsCheck.add(textColumn)
                    linearRow.addView(textColumn)
                    countC++
                }
                linearColumn.addView(linearRow)
            }
            else{

                val linearRow = LinearLayout(context)
                linearRow.orientation = LinearLayout.HORIZONTAL
                for(j in 0 until countColumn)
                {
                    val checkBox  = CheckBox(context)
                    checkBox.width= 100f.toDips().toInt()
                    checkBox.height = 50f.toDips().toInt()
                    checkBox.textSize = 16f
                    checkBox.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    if(typeReport!="0")
                        checkBox.isChecked = listEData[countCheck]
                    if(j==0){
                        val params4 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        val text = TextView(context)
                        text.setTextColor(resources.getColor(R.color.colorTextAll))
                        text.textSize = 16f
                        params4.rightMargin = 1f.toDips().toInt()
                        params4.topMargin = 0f.toDips().toInt()
                        params4.width = 80f.toDips().toInt()
                        //params4.height = 20f.toDips().toInt()
                        text.layoutParams = params4
                        // text.setPadding(0,(30f).toDips().toInt(),(10f).toDips().toInt(),0)
                        text.gravity = Gravity.CENTER_VERTICAL
                        text.text = listRows[count]
                        val listTag =ArrayList<String>(2)
                        listTag.add(listId[count])
                        listTag.add(listIdQuestion[count])
                        listTag.add(listNameQ[count])
                        text.tag = listTag
                        listRowsCheck.add(text)
                        linearRow.addView(text)
                    }
                        //checkBox.text = listRows[count]
                   checkBox.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    val listTag =ArrayList<String>(2)
                    listTag.add(listEId[countCheck])
                    listTag.add(listEIdQuestion[countCheck])
                    listTag.add(listENameQ[countCheck])
                    checkBox.tag = listTag
                    listMultiCheck.add(checkBox)
                    linearRow.addView(checkBox)
                    countCheck++
                }
                count++
                linearColumn.addView(linearRow)
            }
        }
        scroll.layoutParams = paramsMulti
        scroll.addView(linearColumn)
        linearL.addView(scroll)
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorMultiCheck.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)
    }

    private fun addAnswersMultiInput(listAnswers:List<Answer>, linear: LinearLayout, nameQ:String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 10f.toDips().toInt())
        params1.bottomMargin  = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        val scroll = HorizontalScrollView(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
        textView.textSize = 16f
        textView.layoutParams = params1
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
        linearL.addView(textView)
        val linearColumn = LinearLayout(context)
        linearColumn.orientation = LinearLayout.VERTICAL
        val listColumns  = arrayListOf<String>()
        val listRows  = mutableListOf<String>()
        val listElements  = arrayListOf<String>()
        val listId  = arrayListOf<String>()
        val listIdQuestion = arrayListOf<String>()
        val listNameQ = arrayListOf<String>()
        val listData = arrayListOf<String>()
        val listCId  = arrayListOf<String>()
        val listCIdQuestion = arrayListOf<String>()
        val listCNameQ = arrayListOf<String>()
        val listEId  = arrayListOf<String>()
        val listEIdQuestion = arrayListOf<String>()
        val listENameQ = arrayListOf<String>()
        val listEData = arrayListOf<String>()
        for(list in listAnswers.sortedBy { it.order })
        {
            if(list.column)
            {
                listColumns.add(list.answerName)
                listCId.add(list.id)
                listCIdQuestion.add(list.idQuestion)
                listCNameQ.add(nameQ)
            }
            if(list.row)
            {
                listRows.add(list.answerName)
                listId.add(list.id)
                listIdQuestion.add(list.idQuestion)
                listNameQ.add(nameQ)
                listData.add(list.data)
            }
            if(!list.row&&!list.column)
            {
                listElements.add(list.answerName)
                listEId.add(list.id)
                listEIdQuestion.add(list.idQuestion)
                listENameQ.add(nameQ)
                listEData.add(list.data)
            }
        }
        val countRow  = listRows.distinct().count()+1
        val countColumn  = listColumns.count()
        var count = 0
        var countInput = 0
        for(i in 0 until countRow)
        {
            if(i==0)
            {
                val linearRow = LinearLayout(context)
                linearRow.orientation = LinearLayout.HORIZONTAL
                var countC = 0
                val params5 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params5.rightMargin = 1f.toDips().toInt()
                val textColumn = TextView(context)
                textColumn.width = 120f.toDips().toInt()
                textColumn.textSize = 16f
                params2.rightMargin = 5f.toDips().toInt()
                textColumn.layoutParams = params2
                textColumn.setTextColor(resources.getColor(R.color.colorTextAll))
                textColumn.height = 80f.toDips().toInt()
                textColumn.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                textColumn.text = ""
                linearRow.addView(textColumn)
                listColumns.forEach {
                    val textColumn = TextView(context)
                    textColumn.width = 100f.toDips().toInt()
                    textColumn.textSize = 16f
                    params2.rightMargin = 10f.toDips().toInt()
                    textColumn.layoutParams = params2
                    textColumn.setTextColor(resources.getColor(R.color.colorTextAll))
                    textColumn.height = 80f.toDips().toInt()
                    textColumn.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                    textColumn.text = it
                    val listTag =ArrayList<String>(2)
                    listTag.add(listCId[countC])
                    listTag.add(listCIdQuestion[countC])
                    listTag.add(listCNameQ[countC])
                    textColumn.tag = listTag
                    listColumnsInput.add(textColumn)
                    linearRow.addView(textColumn)
                    countC++
                }
                linearColumn.addView(linearRow)
            }
            else{

                val linearRow = LinearLayout(context)
                linearRow.orientation = LinearLayout.HORIZONTAL
                for(j in 0 until countColumn)
                {
                    val editText  = EditText(context)
                    params2.rightMargin = 10f.toDips().toInt()
                    params2.height = 50f.toDips().toInt()
                    params2.width = 100f.toDips().toInt()
                    //editText.width= 80f.toDips().toInt()
                   // editText.height = 50f.toDips().toInt()
                    editText.setBackgroundResource(R.drawable.rounded)
                    editText.setPadding((8f).toDips().toInt(),0,0,0)
                    editText.layoutParams = params2
                    editText.inputType = InputType.TYPE_CLASS_NUMBER

                    editText.hint = "números"
                    editText.textSize = 16f
                    if(typeReport!="0")
                       editText.setText(listEData[countInput])
                    if(j==0){
                        val params4 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        val text = TextView(context)
                        text.setTextColor(resources.getColor(R.color.colorTextAll))
                        text.textSize = 16f
                        params4.rightMargin = 5f.toDips().toInt()
                        text.gravity = TextView.TEXT_ALIGNMENT_CENTER
                        params4.width = 120f.toDips().toInt()
                        //params4.height = 20f.toDips().toInt()
                        text.layoutParams = params4
                       // text.setPadding(0,(30f).toDips().toInt(),(10f).toDips().toInt(),0)
                        text.text = listRows[count]
                        val listTag =ArrayList<String>(2)
                        listTag.add(listId[count])
                        listTag.add(listIdQuestion[count])
                        listTag.add(listNameQ[count])
                        text.tag = listTag
                        listRowsInput.add(text)
                        linearRow.addView(text)
                    }
                        //editText.text = listRows[count]
                    val listTag =ArrayList<String>(2)
                    listTag.add(listEId[countInput])
                    listTag.add(listEIdQuestion[countInput])
                    listTag.add(listENameQ[countInput])
                    editText.tag = listTag
                    listMultiText.add(editText)
                    linearRow.addView(editText)
                    countInput++
                }
                count++
                linearColumn.addView(linearRow)
            }
        }
        scroll.layoutParams = paramsMulti
        scroll.addView(linearColumn)
        linearL.addView(scroll)
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorMultiInput.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)
    }


    private fun addAnswersCheck(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order:String,required:Boolean){
            params.setMargins(0,0, 0, 20f.toDips().toInt())
            val linearL = LinearLayout(context)
            linearL.orientation = LinearLayout.VERTICAL
            val textView  = TextView(context)
            textView.text = "$order. Pregunta: $nameQ"
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
            val textView1 = TextView(context)
            textView1.tag = listAnswers[0].idQuestion
            textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
            textView1.text = textError
            textView1.isVisible = false
            listErrorCheck.add(textView1)
            linearL.addView(textView1)
            linearL.layoutParams = params
            linear.addView(linearL)

    }

    private fun addAnswersCheckBd(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorCheckBd.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersInput(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
            val listTag =ArrayList<String>(3)
            editText.hint = list.answerName.toLowerCase()
            listTag.add(list.id)
            listTag.add(list.idQuestion)
            listTag.add(nameQ)
            listTag.add(required.toString())
            editText.tag = listTag
            if(typeReport!="0")
                editText.setText(list.data)
            listInput.add(editText)
            linearL.addView(editText)
            val textView1 = TextView(context)
            textView1.tag = list.idQuestion
            textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
            textView1.text = textError
            textView1.isVisible = false
            listErrorText.add(textView1)
            linearL.addView(textView1)
        }
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersInputNumber(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorNumber.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersDate(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorDate.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersTime(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorTime.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersPhoto(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
            val params2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params2.topMargin = 30
            params2.width = 300f.toDips().toInt()
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
                    image.setImageURI(getImageUri(requireContext(),decodeBase64(list.data)))
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
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorPhoto.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addAnswersSelectImage(listAnswers:List<Answer>,linear: LinearLayout,nameQ: String,order:String,required:Boolean){
        params.setMargins(0,0, 0, 20f.toDips().toInt())
        params1.bottomMargin = 5f.toDips().toInt()
        val linearL = LinearLayout(context)
        linearL.orientation = LinearLayout.VERTICAL
        val textView  = TextView(context)
        textView.text = "$order. Pregunta: $nameQ"
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
            val params2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params2.width = 300f.toDips().toInt()
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
                    image.setImageURI(getImageUri(requireContext(),decodeBase64(list.data)))
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
        val textView1 = TextView(context)
        textView1.tag = listAnswers[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorImageSelect.add(textView1)
        linearL.addView(textView1)
        linearL.layoutParams = params
        linear.addView(linearL)

    }

    private fun addTwoButtonsAudio(listAnswer: List<Answer>,view : LinearLayout, question: String, order: String) {
        params.setMargins(0,0,0,20f.toDips().toInt())
        params.bottomMargin = 10f.toDips().toInt()

        val linearLayoutContainer = LinearLayout(requireContext())
        linearLayoutContainer.orientation = LinearLayout.VERTICAL

        val tv = TextView(requireContext())
        tv.text = "$order. Pregunta: $question"
        tv.textSize = 16f
        tv.layoutParams = params
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))

        linearLayoutContainer.addView(tv)

        for(answer in listAnswer){

            val recordAudioViews = RecordAudioViews(answer.idQuestion,answer.id, answer.nameQuestion,null,null,null,null,null,null,
                null,null,null,null,null,null,null,"","","")

            val layoutTwoButtons = LinearLayout(requireContext())
            val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            val tagLayoutContainer = "lyContainer${answer.idQuestion}"
            layoutTwoButtons.orientation = LinearLayout.VERTICAL
            layoutTwoButtons.tag = tagLayoutContainer
            paramsTV.setMargins(0,0,0,32f.toDips().toInt())

            //Creando View
            val titleSelected = TextView(requireContext())
            titleSelected.text = "Seleciona un audio"
            titleSelected.textSize = 16f
            titleSelected.layoutParams = paramsTV
            titleSelected.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            recordAudioViews.layoutContainer = layoutTwoButtons
            layoutTwoButtons.addView(titleSelected)


            //Creando Selecionnar audio

            creatingSelectedAudioLayout(answer,layoutTwoButtons,answer.idQuestion,recordAudioViews)


            //Creando TitleRecordAudio
            val paramsTitleRecordAudio = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            paramsTitleRecordAudio.setMargins(0,15f.toDips().toInt(),0,5f.toDips().toInt())
            val titleRecordAudio = TextView(requireContext())
            titleRecordAudio.text = "Grabar un audio"
            titleRecordAudio.textSize = 16f
            titleRecordAudio.layoutParams = paramsTitleRecordAudio
            titleRecordAudio.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            layoutTwoButtons.addView(titleRecordAudio)

            //CreandoAdivise7min
            val paramsTitleAdvise = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            paramsTitleAdvise.setMargins(0,0,0,36f.toDips().toInt())
            val titleAdvise = TextView(requireContext())
            titleAdvise.text = getString(R.string.time_audio_limit)
            titleAdvise.layoutParams = paramsTitleAdvise
            layoutTwoButtons.addView(titleAdvise)

            creatingAudioRecordLayout(answer,layoutTwoButtons,answer.idQuestion,recordAudioViews)


            Log.d("object", recordAudioViews.toString())
            mutableHashMapTag[tagLayoutContainer] = recordAudioViews
            linearLayoutContainer.addView(layoutTwoButtons)
        }


        linearLayoutContainer.layoutParams = params
        //Cmapo obligatorio
        val textView1 = TextView(context)
        textView1.tag = listAnswer[0].idQuestion
        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrors))
        textView1.text = textError
        textView1.isVisible = false
        listErrorAudio.add(textView1)

        linearLayoutContainer.addView(textView1)

        view.addView(linearLayoutContainer)
    }

    private fun creatingAudioRecordLayout(answer: Answer,parent: LinearLayout, questions:String, recordAudioViews: RecordAudioViews){
        //Creating Record button
        val layoutRecordingAudio = LinearLayout(requireContext())
        val paramsLayoutRecording = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        val tagLayout = "ly${questions}"
        layoutRecordingAudio.tag = tagLayout
        layoutRecordingAudio.orientation = LinearLayout.HORIZONTAL
        layoutRecordingAudio.layoutParams = paramsLayoutRecording
        layoutRecordingAudio.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_blue_button_reports)
        layoutRecordingAudio.gravity = Gravity.CENTER
        layoutRecordingAudio.setPadding(16f.toDips().toInt(),16f.toDips().toInt(),16f.toDips().toInt(),16f.toDips().toInt())
        layoutRecordingAudio.setOnClickListener {
            if (mutableHashMapTag[parent.tag.toString()]?.answerSelected == "") {
                if (playerRecorded != null) {
                    stopPlayingAudioRecorded(parent.tag.toString())
                } else {
                    checkPermissions()
                    recordingAudio(parent.tag.toString())
                }
            }else{
                notify(requireActivity(),R.string.only_one_audio_per_question)
            }

        }
        recordAudioViews.layoutRecordButton = layoutRecordingAudio

        //Creating AppCompatImageVIew
        val imageView = ImageView(requireContext())
        val paramsImageView = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        imageView.setImageResource(R.drawable.ic_record)
        imageView.layoutParams = paramsImageView
        layoutRecordingAudio.addView(imageView)

        //Creatin TextView Recording
        val paramsRecordingtv = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        paramsRecordingtv.setMargins(0,0,0,5f.toDips().toInt())
        val tvRecording = TextView(requireContext())
        tvRecording.text = "Grabar un audio"
        tvRecording.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        val tagRecordingtv = "tv${questions}"
        tvRecording.tag = tagRecordingtv
        recordAudioViews.textViewRecording =  tvRecording
        layoutRecordingAudio.addView(tvRecording)

        //Creating Chronometer
        val chronometer = Chronometer(requireContext())
        val paramsChronometer = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        paramsChronometer.setMargins(20f.toDips().toInt(),0,5f.toDips().toInt(),0)
        chronometer.isAllCaps = false
        chronometer.visibility = View.GONE
        chronometer.textSize = 16f
        chronometer.gravity = Gravity.CENTER
        chronometer.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        chronometer.layoutParams = paramsChronometer
        val tagChronometer = "ch${questions}"
        chronometer.tag = tagChronometer
        recordAudioViews.chrometerRecording = chronometer
        layoutRecordingAudio.addView(chronometer)


        //Creando grabacion - Clip
        val layoutShowAudioRecorded= LinearLayout(requireContext())
        val paramsLayoutAudioRecorded = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        paramsLayoutAudioRecorded.topMargin = 10f.toDips().toInt()
        val tagLayoutShowRecorded= "lyClip${questions}"
        layoutShowAudioRecorded.tag = tagLayoutShowRecorded
        layoutShowAudioRecorded.orientation = LinearLayout.HORIZONTAL
        layoutShowAudioRecorded.layoutParams = paramsLayoutAudioRecorded
        layoutShowAudioRecorded.gravity = Gravity.CENTER
        if(answer.data == ""){
            layoutShowAudioRecorded.visibility = View.GONE
        } else {
            Log.d("img",answer.path)
            if (typeReport == "Terminado") {
                val (type, path) = getTypeAndPathRecord(answer.path)
                if(type == TYPE_RECCORDED){
                    recordAudioViews.answerRecorded = path
                    hashPathAudioRecorded[parent.tag.toString()] = path
                }
                else layoutShowAudioRecorded.visibility = View.GONE
            } else {
                val (type, path) = getTypeAndPathRecord(answer.data)
                if (type == TYPE_RECCORDED) {
                    recordAudioViews.answerRecorded = path
                    hashPathAudioRecorded[parent.tag.toString()] = path
                } else {
                    layoutShowAudioRecorded.visibility = View.GONE
                }
            }
        }
        recordAudioViews.layoutShowRecord = layoutShowAudioRecorded

        //Creating AppCompatImageVIew
        val imageViewClip = ImageView(requireContext())
        val paramsImageViewClip = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        imageViewClip.setImageResource(R.drawable.ic_clip)
        imageViewClip.layoutParams = paramsImageViewClip
        layoutShowAudioRecorded.addView(imageViewClip)

        //Creatin TextView Recording
        val paramsRecordedText = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        paramsRecordedText.setMargins(20f.toDips().toInt(),0,15f.toDips().toInt(),0)
        val tvRecordedText = TextView(requireContext())
        tvRecordedText.text = "Grabación 1"
        tvRecordedText.layoutParams = paramsRecordedText
        tvRecordedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        tvRecordedText.setOnClickListener {
            playingAudioRecorded(parent.tag.toString())
        }
        val tagRecordedText = "tvClip${questions}"
        tvRecordedText.tag = tagRecordedText
        layoutShowAudioRecorded.addView(tvRecordedText)
        recordAudioViews.textViewShowRecord =  tvRecordedText

        //Creating ImageView
        val imageViewClipClose= ImageView(requireContext())
        val paramsImageViewClipClose = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        imageViewClipClose.setImageResource(R.drawable.ic_close)
        imageViewClipClose.layoutParams = paramsImageViewClipClose
        val tagIvClose = "ivClip${questions}"
        imageViewClipClose.tag = tagIvClose
        imageViewClipClose.setOnClickListener {
            deleteAudioRecorded(parent.tag.toString(), TYPE_RECCORDED)
        }
        recordAudioViews.imageViewClipClose = imageViewClipClose
        layoutShowAudioRecorded.addView(imageViewClipClose)

        parent.addView(layoutRecordingAudio)
        parent.addView(layoutShowAudioRecorded)

    }

    private fun creatingSelectedAudioLayout(answer: Answer,parent: LinearLayout, questions:String, recordAudioViews: RecordAudioViews){
        //Creating Selected  button
        val layoutSelectedAudio = LinearLayout(requireContext())
        val paramsLayoutRecording = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        val tagLayout = "ly$questions"
        layoutSelectedAudio.tag = tagLayout
        layoutSelectedAudio.orientation = LinearLayout.HORIZONTAL
        layoutSelectedAudio.layoutParams = paramsLayoutRecording
        layoutSelectedAudio.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_blue_button_reports)
        layoutSelectedAudio.gravity = Gravity.CENTER
        layoutSelectedAudio.setPadding(16f.toDips().toInt(),16f.toDips().toInt(),16f.toDips().toInt(),16f.toDips().toInt())
        recordAudioViews.layoutAudioSelectedButton = layoutSelectedAudio
        layoutSelectedAudio.setOnClickListener {
            if(mutableHashMapTag[parent.tag.toString()]?.answerRecorded == ""){
                if (playerSelected != null) stopPlayingAudioSelected(parent.tag.toString())
                else {
                    checkPermissions()
                    currentLayout = parent.tag.toString()
                    responseLauncher.launch("audio/*")
                }
            }else{
                notify(requireActivity(),R.string.only_one_audio_per_question)
            }
        }

        //Creating AppCompatImageVIew
        val imageViewSelectedButton = ImageView(requireContext())
        val paramsImageView = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        imageViewSelectedButton.setImageResource(R.drawable.ic_record)
        imageViewSelectedButton.layoutParams = paramsImageView
        layoutSelectedAudio.addView(imageViewSelectedButton)

        //Creatin TextView Recording
        val tvSelectedAudio = TextView(requireContext())
        val paramsSelectedTv = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        paramsSelectedTv.setMargins(0,0,0,5f.toDips().toInt())
        val tagRecordingtv = "tv$questions"
        tvSelectedAudio.text = "Selecciona un audio"
        tvSelectedAudio.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        tvSelectedAudio.tag = tagRecordingtv
        recordAudioViews.textViewAudioSelected = tvSelectedAudio
        layoutSelectedAudio.addView(tvSelectedAudio)

        //Creating Chronometer
        val chronometerSelectedAudio = Chronometer(requireContext())
        val paramsChronometer = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        paramsChronometer.setMargins(20f.toDips().toInt(),0,5f.toDips().toInt(),0)
        chronometerSelectedAudio.isAllCaps = false
        chronometerSelectedAudio.visibility = View.GONE
        chronometerSelectedAudio.textSize = 16f
        chronometerSelectedAudio.gravity = Gravity.CENTER
        chronometerSelectedAudio.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        chronometerSelectedAudio.layoutParams = paramsChronometer
        val tagChronometer = "ch$questions}"
        chronometerSelectedAudio.tag = chronometerSelectedAudio
        recordAudioViews.chrometerSeleceted = chronometerSelectedAudio
        layoutSelectedAudio.addView(chronometerSelectedAudio)


        //Layout AudioSelkected view - clip
        val layoutAudioSelectedClip = LinearLayout(requireContext())
        val paramslayoutAudioSelected = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        paramslayoutAudioSelected.topMargin = 10f.toDips().toInt()
        val tagLayoutAudioSelected = "lyClip${questions}"
        layoutAudioSelectedClip.tag = tagLayoutAudioSelected
        layoutAudioSelectedClip.orientation = LinearLayout.HORIZONTAL
        layoutAudioSelectedClip.layoutParams = paramslayoutAudioSelected
        layoutAudioSelectedClip.gravity = Gravity.CENTER
        if(answer.data == ""){
            layoutAudioSelectedClip.visibility = View.GONE
        }else{

            if (typeReport == "Terminado") {
                val (type,path) = getTypeAndPathRecord(answer.path)
                if(type == TYPE_SELECTED){
                    recordAudioViews.answerSelected = path
                    hashPathAudioSelected[parent.tag.toString()] = path
                }
                else layoutAudioSelectedClip.visibility = View.GONE
            }else{
                val (type,path) = getTypeAndPathRecord(answer.data)
                if(type == TYPE_SELECTED){
                    recordAudioViews.answerSelected = path
                    hashPathAudioSelected[parent.tag.toString()] = path
                }
                else layoutAudioSelectedClip.visibility = View.GONE
            }
        }
        recordAudioViews.layoutShowSelectedAudio =  layoutAudioSelectedClip

        //Creating AppCompatImageVIew
        val imageViewClipSelected = ImageView(requireContext())
        val paramsImageViewClipSelected = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        imageViewClipSelected.setImageResource(R.drawable.ic_clip)
        imageViewClipSelected.layoutParams = paramsImageViewClipSelected
        layoutAudioSelectedClip.addView(imageViewClipSelected)

        //Creatin TextView Recording
        val paramsSelectedText = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        paramsSelectedText.setMargins(20f.toDips().toInt(), 0, 15f.toDips().toInt(), 0)
        val tagSelectedText = "tvClip${questions}"
        val tvSelectedTxt = TextView(requireContext())
        if(answer.data != "") tvSelectedTxt.text = getNameInSelectedFile(answer.path)
        else tvSelectedTxt.text = "Audio Seleccionado"
        tvSelectedTxt.layoutParams = paramsSelectedText
        tvSelectedTxt.tag = tagSelectedText
        tvSelectedTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        tvSelectedTxt.setOnClickListener {
            playingAudioSelected(parent.tag.toString())
        }
        recordAudioViews.textViewShowSelecetedAudio = tvSelectedTxt
        layoutAudioSelectedClip.addView(tvSelectedTxt)

        //Creating ImageView
        val imageViewClipCloseSelected = ImageView(requireContext())
        val tagIvClose = "ivClip${questions}"
        val paramsImageViewClipCloseSelected = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        imageViewClipCloseSelected.setImageResource(R.drawable.ic_close)
        imageViewClipCloseSelected.layoutParams = paramsImageViewClipCloseSelected
        imageViewClipCloseSelected.tag = tagIvClose
        imageViewClipCloseSelected.setOnClickListener {
            deleteAudioRecorded(parent.tag.toString(), TYPE_SELECTED)
        }
        recordAudioViews.imageViewClipCloseSelected = imageViewClipCloseSelected
        layoutAudioSelectedClip.addView(imageViewClipCloseSelected)

        parent.addView(layoutSelectedAudio)
        parent.addView(layoutAudioSelectedClip)
    }

    //Fucniones para grabar audio
    private fun deleteAudioRecorded(tag: String, type:String) {
        mutableHashMapTag[tag]?.let { recordAudio ->
            if(type == TYPE_SELECTED){
                hashPathAudioSelected[tag] = ""
                recordAudio.layoutShowSelectedAudio?.invisible()
                recordAudio.answerSelected = ""
            }else if(type == TYPE_RECCORDED){
                hashPathAudioRecorded[tag] = ""
                recordAudio.answerRecorded = ""
                recordAudio.layoutShowRecord?.invisible()
            }
        }
    }
    private fun recordingAudio(tag: String) {
        mutableHashMapTag[tag]?.let { recordAudio ->
            if (playerRecorded != null) {
                recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                recordAudio.textViewRecording?.visible()
                recordAudio.textViewShowRecord?.visible()
                recordAudio.chrometerRecording?.apply {
                    visibility = View.GONE
                    base = SystemClock.elapsedRealtime()
                    stop()
                    playerRecorded = null
                }
                playerRecorded?.stop()
                playerRecorded?.release()
                playerRecorded = null
            } else {
                val previousRecord = hashPathAudioRecorded[tag] ?: ""
                if (previousRecord == "") {
                    if (recording) {
                        recordAudio.chrometerRecording?.apply {
                            visibility = View.GONE
                            base = SystemClock.elapsedRealtime()
                            stop()
                        }
                        recordAudio.textViewRecording?.visibility = View.VISIBLE
                        recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                        recordAudio.textViewShowRecord?.visibility =View.VISIBLE
                        stopRecord(recordAudio)
                        hashPathAudioRecorded[tag] = recordPath
                        recordAudio.answerRecorded = recordPath
                    } else {
                        val timeLimitString = getMiliSeoncdsOnFormat(audioLimit)
                        recordAudio.textViewRecording?.invisible()
                        recordAudio.textViewShowRecord?.invisible()
                        recordAudio.layoutShowRecord?.invisible()
                        recordAudio.chrometerRecording?.apply {
                            visibility = View.VISIBLE
                            base = SystemClock.elapsedRealtime()
                            setOnChronometerTickListener { chrometer ->
                                if (text.trim() == timeLimitString) {
                                    stop()
                                    onChronometerTickListener = null
                                    visibility = View.GONE
                                    base = SystemClock.elapsedRealtime()
                                    notify(activity, R.string.error_audio_limit)
                                    stopRecord(recordAudio)
                                    recordAudio.textViewRecording?.visibility = View.VISIBLE
                                    recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                                    hashPathAudioRecorded[tag] = recordPath
                                    recordAudio.answerRecorded = recordPath
                                }
                            }
                            start()
                        }
                        startRecord(recordAudio)
                    }
                } else {
                    val conditionalFragment = ConditionalDialogFragment.newInstance(
                        title = "El audio anterior será borrado",
                        message = "¿Desea sobrescribir el audio que ya se grabó?",
                        icon = R.drawable.ic_warning,
                        btn1 = "Sí",
                        btn2 = "No"
                    )
                    conditionalFragment.listener = object : ConditionalDialogFragment.Callback {
                        override fun onAccept() {
                            hashPathAudioRecorded[tag] = ""
                            recordingAudio(tag)
                        }
                    }
                    conditionalFragment.show(childFragmentManager, "dialog")
                }

            }
        }
    }
    private fun startRecord(recordAudio : RecordAudioViews) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            val outputFolder = File(Environment.getExternalStorageDirectory().toString() + "/download/")
            val output = File(outputFolder.absolutePath + "/record" + Date().time + ".3gpp")

            Log.i("DIRECTORIO", output.absolutePath)

            recordPath = output.absolutePath
            setOutputFile(output.absolutePath)
            setMaxDuration(420000)
            try {
                prepare()
                start()
                recording = true
            } catch (e: IOException) {
                errorRecording = true
                recorder = null
                recording = false
                recordAudio.chrometerRecording?.apply {
                    visibility = View.GONE
                    base = SystemClock.elapsedRealtime()
                    stop()
                }
                recordAudio.textViewShowRecord?.visibility = View.VISIBLE
                recordAudio.textViewRecording?.visibility = View.VISIBLE
                recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                Log.e("RECORD", e.toString())
                val msg = MessageDialogFragment.newInstance("Ha ocurrido un error al grabar el audio")
                msg.show(childFragmentManager, "dialog")
            }
        }
    }
    private fun stopRecord(recordAudio : RecordAudioViews) {
        if(recorder  != null){
            recorder?.let {
                it.stop()
                it.release()
                recording = false
            }
            recorder = null
        }
    }
    private fun playingAudioRecorded(tag: String) {
        mutableHashMapTag[tag]?.let { recordAudio ->
            hashPathAudioRecorded[tag]?.let { path ->
                try {
                    playerRecorded = MediaPlayer().apply {
                        /*if (isPlaying) {
                            recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                            recordAudio.textViewRecording?.visible()
                            recordAudio.textViewShowRecord?.visible()
                            recordAudio.chrometerRecording?.apply {
                                visibility = View.GONE
                                base = SystemClock.elapsedRealtime()
                                stop()
                                playerRecorded = null
                            }
                            stop()
                            release()
                        }*/
                        setDataSource(path)
                        prepare()
                        setOnCompletionListener {
                            recordAudio.chrometerRecording?.apply {
                                visibility = View.GONE
                                base = SystemClock.elapsedRealtime()
                                stop()
                            }
                            playerRecorded = null
                            recordAudio.textViewShowRecord?.visible()
                            recordAudio.layoutShowRecord?.visible()
                            recordAudio.textViewRecording?.visible()
                        }
                        start()
                        recordAudio.textViewShowRecord?.invisible()
                        recordAudio.layoutShowRecord?.invisible()
                        recordAudio.textViewRecording?.invisible()
                        recordAudio.chrometerRecording?.apply {
                            visibility = View.VISIBLE
                            base = SystemClock.elapsedRealtime()
                            start()
                        }
                        //disableButtons(DISABLED,recordAudio.layoutRecordButton!!)
                    }
                } catch (e: IOException) {
                    Log.e("PLAYING ERROR", " $e \n No se encuentra ael recordpat ->$path")
                }
            }
        }
    }
    private fun playingAudioSelected(tag:String){
        mutableHashMapTag[tag]?.let { recordAudio ->
            try {
                playerSelected = MediaPlayer().apply {
                    val path = hashPathAudioSelected[tag]?.toLowerCase(Locale.ROOT)
                    setDataSource(path)
                    prepare()
                    setOnCompletionListener {
                        recordAudio.chrometerSeleceted?.apply {
                            visibility = View.GONE
                            base = SystemClock.elapsedRealtime()
                            stop()
                        }
                        playerSelected= null
                        recordAudio.textViewAudioSelected?.visibility = View.VISIBLE
                        recordAudio.layoutShowSelectedAudio?.visibility = View.VISIBLE
                    }
                    start()
                    //playingAudioSecleted = true
                    recordAudio.layoutShowSelectedAudio?.invisible()
                    recordAudio.textViewAudioSelected?.invisible()
                    recordAudio.chrometerSeleceted?.apply {
                        visibility = View.VISIBLE
                        base = SystemClock.elapsedRealtime()
                        start()
                    }
                    //disableButtons(DISABLED,recordAudio.layoutAudioSelectedButton!!)
                }
            } catch (e: IOException) {
                val message =
                    MessageDialogFragment.newInstance("No se puede reproducir el audio seleccionado debido que es un formato no compatible")
                message.show(childFragmentManager,"")
                Log.e("PLAYING ERROR", " $e \n No se encuentra ael recordpat -> ")
            }
        }
    }
    private fun stopPlayingAudioSelected(tag:String){
        mutableHashMapTag[tag]?.let { recordAudio ->
            //Log.d("player",playingAudioSecleted.toString())
            playerSelected.apply {
                recordAudio.chrometerSeleceted?.apply {
                    visibility = View.GONE
                    base = SystemClock.elapsedRealtime()
                    stop()
                }
                this?.stop()
                this?.release()
                playerSelected = null
                //disableButtons(ENABLED,recordAudio.layoutAudioSelectedButton!!)
                recordAudio.textViewAudioSelected?.visibility = View.VISIBLE
                recordAudio.layoutShowSelectedAudio?.visibility = View.VISIBLE

            }
        }
    }
    private fun stopPlayingAudioRecorded(tag:String){
        mutableHashMapTag[tag]?.let { recordAudio ->
            //Log.d("player",playingAudioSecleted.toString())
            playerRecorded.apply {
                recordAudio.chrometerRecording?.apply {
                    visibility = View.GONE
                    base = SystemClock.elapsedRealtime()
                    stop()
                }
                this?.stop()
                this?.release()
                playerRecorded = null
                //disableButtons(ENABLED,recordAudio.layoutRecordButton!!)
                recordAudio.textViewRecording?.visibility = View.VISIBLE
                recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                recordAudio.textViewShowRecord?.visibility = View.VISIBLE
            }
        }
    }
    private fun getMiliSeoncdsOnFormat(timeMiliseconds: Int): String {
        val seconds = (timeMiliseconds / 1000) % 60
        val minutes = timeMiliseconds / (1000 * 60)
        return if (seconds < 10) {
            "0$minutes:0$seconds"
        } else {
            "0$minutes:$seconds"
        }
    }
    private fun getTypeAndPathRecord(data:String): Pair<String,String>{
        data.apply {
            val type = substring(0,indexOf("&",0,true))
            val path = substring(indexOf("&",0,true)+1, data.length)
            return Pair(type,path)
        }
    }

    private fun convertImageFileToBase64(file: File): String {
        return ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                file.inputStream().use { inputStream ->
                    inputStream.copyTo(base64FilterStream)
                }
            }
            return@use outputStream.toString()
        }
    }
    private fun getNameInSelectedFile(data:String): String{
        data.apply { return substring(lastIndexOf("/",length,true)+1,length) }
    }
    private fun disableButtons(type:String, except: LinearLayout){
        mutableHashMapTag.let {
            for(recordView in mutableHashMapTag.values){
                if(type== ENABLED){
                    recordView.layoutRecordButton?.isEnabled = true
                    recordView.layoutAudioSelectedButton?.isEnabled = true
                }else{
                    recordView.layoutRecordButton?.isEnabled = false
                    recordView.layoutAudioSelectedButton?.isEnabled = false
                }
            }
            except.isEnabled = true
        }
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
            et.setText( SimpleDateFormat("HH:mm").format(cal.time))
        }
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "IMG_" + System.currentTimeMillis(),
            null
        )
        return Uri.parse(path)
    }

    private fun decodeBase64(base64:String):Bitmap{
        val encodedString = "data:image/jpg;base64, $base64"
        val pureBase64Encoded: String = encodedString.substring(encodedString.indexOf(",") + 1)
        val decodedString: ByteArray = Base64.decode(pureBase64Encoded, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
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
        val bb =Bitmap.createScaledBitmap(bm, 400, 800, false)
        val outputStream = ByteArrayOutputStream()
        bb.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
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

    private fun hideTextError(){
        listErrorText.forEach { it.isVisible = false }
        listErrorRadio.forEach { it.isVisible = false }
        listErrorCheck.forEach { it.isVisible = false }
        listErrorCheckBd.forEach { it.isVisible = false }
        listErrorRadioBd.forEach { it.isVisible = false }
        listErrorList.forEach { it.isVisible = false }
        listErrorListBd.forEach { it.isVisible = false }
        listErrorNumber.forEach { it.isVisible = false }
        listErrorDate.forEach { it.isVisible = false }
        listErrorTime.forEach { it.isVisible = false }
        listErrorPhoto.forEach { it.isVisible = false }
        listErrorImageSelect.forEach { it.isVisible = false }
        listErrorMultiRadio.forEach { it.isVisible = false }
        listErrorMultiCheck.forEach { it.isVisible = false }
        listErrorMultiInput.forEach { it.isVisible = false }
        listErrorAudio.forEach { it.isVisible = false }
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
                    tag[2] = convertBase64(urlImage)?:""
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
                    tag[2] = convertBase64(realPath.toString())?:""
                    image.isVisible  = true
                    listViewsSelect[index].isVisible = true
                    val imageUri = Uri.fromFile(File(realPath))
                    image.setImageURI(imageUri)
                }
            }

        }
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
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

    private fun getFilename(uri: Uri): String {
        val cursor = activity?.contentResolver?.query(uri, null, null, null, null)
        var filename: String? = null

        cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)?.let { nameIndex ->
            cursor.moveToFirst()

            filename = cursor.getString(nameIndex)
            cursor.close()
        }

        return filename ?: "Audio"
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