package com.tawa.allinapp.features.reports.audiodinamico

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.ConditionalDialogFragment
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.invisible
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.extensions.visible
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentDynamicAudioBinding
import com.tawa.allinapp.features.reports.audiodinamico.usecase.RecordAudioViews
import com.tawa.allinapp.models.Answer
import java.io.File
import java.io.IOException


class DynamicAudioFragment : BaseFragment() {
    private lateinit var binding: FragmentDynamicAudioBinding
    private lateinit var dynamicAudioViewModel: DynamicAudioViewModel
    private var idReport: String? = null
    private var reportType:String? =""
    private var params= LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
    private var param1= LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
    private var param2= LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
    private var listRecordAudioButton: ArrayList<Button> = ArrayList()
    private var listSelectedAudioButton: ArrayList<Button> = ArrayList()
    private var listAnswers : ArrayList<Answer> = ArrayList()
    //RecordAudio
    private var audioLimit: Int = 1000 * 60 * 1 // 7 minutos
    private var mutableHashMapTag: MutableMap<String,RecordAudioViews> = mutableMapOf()
    private var hashPathAudioRecorded: MutableMap<String,String> = mutableMapOf()
    private var hashPathAudioSelected: MutableMap<String,String> = mutableMapOf()
    private var currentLayout: String = ""

    companion object{
        val TYPE_SELECTED = "SELECTED"
        val TYPE_RECCORDED = "RECORDED"
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
                        it.answerSelected = realPath
                        //it.textViewAudioSelected?.visibility = View.GONE
                        it.layoutShowSelectedAudio?.visibility = View.VISIBLE
                        it.textViewShowSelecetedAudio?.text = audioSelectedName
                        hashPathAudioSelected[currentLayout] = realPath
                    }
                    //val converted = dynamicAudioViewModel.convertAudioSelectedTo64Format(realPath.toString())
                }catch (e : Exception){
                    Log.d("exception", e.toString())
                }
            }
        }
    }

    private var recordingAudio : Boolean = false
    private var player : MediaRecorder ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDynamicAudioBinding.inflate(inflater)

        dynamicAudioViewModel = viewModel(viewModelFactory){
            observe(getStateReport,{
                if(it?.isNotEmpty() == true){
                    getLastLocation()
                    reportType = it
                    idReport?.let { id ->
                        dynamicAudioViewModel.getQuestions(idReport = id)
                    }
                }
            })
            observe(getQuestions,{
                it?.let { answersList ->
                    Log.d("answers", answersList.toString())
                    listAnswers.addAll(it)

                    val listFiltered = ArrayList(answersList).distinctBy { answers ->
                        answers.nameQuestion
                    }
                    Log.d("filtered", listFiltered.toString())
                    for(list in listFiltered){
                        Log.d("ans", list.toString())
                        val listAnswer = answersList.filter{ ans -> ans.nameQuestion == list.nameQuestion }
                        addTwoButtonsAudio(listAnswer,binding.contentCheckList,list.nameQuestion, list.orderQ)
                    }

                }
            })
            observe(setAnswers,{
                it?.let {
                    if(it){
                        notify(requireActivity(),R.string.successful_update_audio)
                    }
                }
            })
            observe(failure,{
                val frag = MessageDialogFragment.newInstance(failure.toString())
                frag.show(childFragmentManager, "dialog")
            })
        }

        arguments?.getString("id").toString().also {idRep->
            idReport=idRep
            idReport?.let {
                dynamicAudioViewModel.getStateReport(it,1)
            }
        }

        arguments?.getString("name").toString().also {
            binding.tvTitleStandard.text = it
        }

        binding.btnBr.setOnClickListener {
            Log.d("mutableHashMap",hashPathAudioRecorded.toString())
            Log.d("mutableHashMap",hashPathAudioSelected.toString())
            Log.d("mutablehashmaptag", mutableHashMapTag.toString())
            for(i in mutableHashMapTag.values){
                if(i.answerRecorded != "" && i.answerSelected != ""){
                    val dialog = MessageDialogFragment.newInstance("Solo se puede guardar un audio por pregunta")
                    dialog.show(childFragmentManager, "dialog")
                    break
                }else{
                    Log.d("list",listAnswers.toString())
                    Log.d("answers", "selected ->" + i.answerSelected + "\nnrecorded ->"+i.answerRecorded)
                    val answerbyQuestions = if(i.answerSelected == "") i.answerRecorded else i.answerSelected
                    dynamicAudioViewModel.setAnswerPvAudio(i.idAnswer,i.idQuestion,answerbyQuestions ?: "","")
                    activity?.onBackPressed()

                }
            }

        }

        return binding.root
    }

    private fun addTwoButtonsAudio(listAnswer: List<Answer>,view : LinearLayout, question: String, order: String) {
        params.setMargins(0,0,0,20f.toDips().toInt())
        param1.bottomMargin = 10f.toDips().toInt()

        val linearLayoutContainer = LinearLayout(requireContext())
        linearLayoutContainer.orientation = LinearLayout.VERTICAL

        val tv = TextView(requireContext())
        tv.text = "$order. Pregunta: $question"
        tv.textSize = 16f
        tv.layoutParams = param1
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))

        linearLayoutContainer.addView(tv)

        for(answer in listAnswer){

            val recordAudioViews = RecordAudioViews(answer.idQuestion,answer.id, null,null,null,null,null,null,
                null,null,null,null,null,null,null,"","")

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
            //listSelectedAudioButton.add(btnSelectedAudio)
            //listRecordAudioButton.add(btnRecordAudio)
            //linearLayoutContainer.addView(layoutTwoButtons)
            //linearLayoutContainer.addView(layoutRecordingAudio)
            linearLayoutContainer.addView(layoutTwoButtons)
        }
        linearLayoutContainer.layoutParams = params
        view.addView(linearLayoutContainer)
    }

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

    private fun Float.toDips() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    private fun recordingAudio(tag: String) {
        mutableHashMapTag[tag]?.let { recordAudio ->
            hashPathAudioRecorded[tag]?.let { Log.d("hashmap", it) }
            val tagLayoutContainer = recordAudio.layoutContainer?.tag
            val previousRecord = hashPathAudioRecorded[tagLayoutContainer] ?: ""
            if(previousRecord == ""){
                val recording = dynamicAudioViewModel.recording
                if (recording == true) {
                    recordAudio.chrometerRecording?.apply {
                        visibility = View.GONE
                        base = SystemClock.elapsedRealtime()
                        stop()
                    }
                    recordAudio.textViewRecording?.visibility = View.VISIBLE
                    recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                    dynamicAudioViewModel.stopRecord()
                    hashPathAudioRecorded[tag] = dynamicAudioViewModel.recordPath
                    recordAudio.answerRecorded = dynamicAudioViewModel.recordPath
                } else {
                    val timeLimitString = getMiliSeoncdsOnFormat(audioLimit)
                    recordAudio.textViewRecording?.invisible()
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
                                dynamicAudioViewModel.stopRecord()
                                recordAudio.textViewRecording?.visibility = View.VISIBLE
                                recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                                hashPathAudioRecorded[tag] = dynamicAudioViewModel.recordPath
                                recordAudio.answerRecorded = dynamicAudioViewModel.recordPath
                            }
                        }
                        start()
                    }
                    dynamicAudioViewModel.startRecord()
                }
            }else{
                val conditionalFragment = ConditionalDialogFragment.newInstance(
                    title = "El audio anterior será borrado",
                    message = "¿Desea sobrescribir el audio que ya se grabó?",
                    icon = R.drawable.ic_warning,
                    btn1 = "Sí",
                    btn2 = "No"
                )
                conditionalFragment.listener = object : ConditionalDialogFragment.Callback {
                    override fun onAccept() {
                        hashPathAudioRecorded[tagLayoutContainer.toString()] = ""
                        recordingAudio(tag)
                    }
                }
                conditionalFragment.show(childFragmentManager, "dialog")
            }
        }
    }

    private fun playingAudioRecorded(tag: String) {
        mutableHashMapTag[tag]?.let { recordAudio ->
            hashPathAudioRecorded[tag]?.let { path ->
//                recordAudio.textViewShowRecord?.invisible()
                try {
                    val playerRecorded = MediaPlayer().apply {
                        setDataSource(path)
                        prepare()
                        setOnCompletionListener {
                            recordAudio.chrometerRecording?.apply {
                                visibility = View.GONE
                                base = SystemClock.elapsedRealtime()
                                stop()
                            }
                            recordAudio.layoutShowRecord?.visibility = View.VISIBLE
                            recordAudio.textViewRecording?.visible()
                            recordAudio.textViewShowRecord?.visible()
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
                    }
                } catch (e: IOException) {
                    Log.e("PLAYING ERROR", " $e \n No se encuentra ael recordpat -> ")
                }
            }
        }
    }

    private fun playingAudioSelected(tag:String){
        mutableHashMapTag[tag]?.let { recordAudio ->
            try {
                val playerRecorded = MediaPlayer().apply {
                    Log.d("recordSelected",hashPathAudioSelected[tag].toString())
                    setDataSource(hashPathAudioSelected[tag])
                    prepare()
                    setOnCompletionListener {
                        recordAudio.chrometerSeleceted?.apply {
                            visibility = View.GONE
                            base = SystemClock.elapsedRealtime()
                            stop()
                        }
                        recordAudio.textViewAudioSelected?.visibility = View.VISIBLE
                        recordAudio.layoutShowSelectedAudio?.visibility = View.VISIBLE
                    }
                    start()
                    recordAudio.layoutShowSelectedAudio?.invisible()
                    recordAudio.textViewAudioSelected?.invisible()
                    recordAudio.chrometerSeleceted?.apply {
                        visibility = View.VISIBLE
                        base = SystemClock.elapsedRealtime()
                        start()
                    }
                }
            } catch (e: IOException) {
                Log.e("PLAYING ERROR", " $e \n No se encuentra ael recordpat -> ")
            }
        }
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
            checkPermissions()
            recordingAudio(parent.tag.toString())
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
        tvRecording.tag = tag
        //hashTextView[tagRecordingtv] = tvRecording
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
        }else{
            hashPathAudioSelected[parent.tag.toString()] = answer.data
        }
        recordAudioViews.layoutShowRecord = layoutShowAudioRecorded
        //hashLayoutShowRecord[tagLayoutShowRecorded] = layoutShowAudioRecorded

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
        //hashTextViewRecord[tagRecordedText] = tvRecordedText
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
        //hashImageVIewClipClose[tagIvClose] =imageViewClipClose
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
            checkPermissions()
            currentLayout =  parent.tag.toString()
            responseLauncher.launch("audio/*")
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
        tvSelectedAudio.tag = tag
        //hashTextView[tagRecordingtv] = tvRecording
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
        chronometerSelectedAudio.tag = tag
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
            hashPathAudioSelected[parent.tag.toString()] = answer.data
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
        tvSelectedTxt.text = "Audio Seleccionado"
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

    private fun getMiliSeoncdsOnFormat(timeMiliseconds: Int): String {
        val seconds = (timeMiliseconds / 1000) % 60
        val minutes = timeMiliseconds / (1000 * 60)
        return if (seconds < 10) {
            "0$minutes:0$seconds"
        } else {
            "0$minutes:$seconds"
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

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }


    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }


    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

}