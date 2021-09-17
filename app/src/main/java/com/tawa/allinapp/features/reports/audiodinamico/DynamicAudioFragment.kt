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
import androidx.core.view.marginTop
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.ConditionalDialogFragment
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.invisible
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.extensions.visible
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentDynamicAudioBinding
import com.tawa.allinapp.features.reports.audiodinamico.usecase.RecordAudio
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
    private var listTagRecordBtn: ArrayList<RecordAudio> = ArrayList()
    private var listAnswers : ArrayList<Answer> = ArrayList()
    //RecordAudio
    private var mutableHashMapTag: MutableMap<String,RecordAudioViews> = mutableMapOf()
    private var hashTextView: MutableMap<String, TextView> = mutableMapOf()
    private var hashChronometer: MutableMap<String,Chronometer> = mutableMapOf()

    private var hashLayoutShowRecord: MutableMap<String,LinearLayout> = mutableMapOf()
    private var hashTextViewRecord : MutableMap<String,TextView> = mutableMapOf()
    private var hashImageVIewClipClose: MutableMap<String,ImageView> = mutableMapOf()
    private var hashPathAudioRecorded: MutableMap<String,String> = mutableMapOf()



    private val responseLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        val realPath = getRealPathFromURI(requireContext(), uri)
        realPath?.let { realPath->
            try{
                Log.d("realPath", realPath.toString())

                val audioSelectedName = getFilename(uri)
                Log.d("audioSelectedNmae",audioSelectedName.toString())
                val converted = dynamicAudioViewModel.convertAudioSelectedTo64Format(realPath.toString())
            }catch (e : Exception){
                Log.d("exception", e.toString())
            }
            //audioSelectedPath = realPath.toString()
            //audioSelected64 = audioViewModel.convertAudioSelectedTo64Format(audioSelectedPath).toString()

            //audioSelectedName = getFilename(uri)

            //binding.tvRecordSelected.text = audioSelectedName
            //binding.lyRecordSelected.visibility = View.VISIBLE
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

        for(list in listAnswer){
            val layoutTwoButtons = LinearLayout(requireContext())
            val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            val tagLayoutContainer = "lyContainer${list.idQuestion}"
            layoutTwoButtons.orientation = LinearLayout.VERTICAL
            layoutTwoButtons.tag = tagLayoutContainer
            paramsTV.setMargins(0,0,0,32f.toDips().toInt())
            //Creando View
            val titleSelected = TextView(requireContext())
            titleSelected.text = "Seleciona un audio"
            titleSelected.textSize = 16f
            titleSelected.layoutParams = paramsTV
            titleSelected.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            layoutTwoButtons.addView(titleSelected)


            //Creando Selecionnar audio
            val btnSelectedAudio = Button(context)
            val paramBtnSelected = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

            paramBtnSelected.bottomMargin = (48f).toDips().toInt()
            btnSelectedAudio.layoutParams = paramBtnSelected
            btnSelectedAudio.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_blue_button_reports)
            btnSelectedAudio.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            btnSelectedAudio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_record,0,R.drawable.ic_recording,0)
            btnSelectedAudio.setPadding((16f).toDips().toInt(),0,(16f).toDips().toInt(),0)
            btnSelectedAudio.height = (50f).toDips().toInt()
            btnSelectedAudio.text = "Seleccionar un audio"
            btnSelectedAudio.setOnClickListener {
                responseLauncher.launch("audio/*")
            }
            val listTag = ArrayList<String>()
            listTag.add(list.id)
            listTag.add(list.idQuestion)
            listTag.add(question)
            btnSelectedAudio.tag = listTag
            layoutTwoButtons.addView(btnSelectedAudio)


            //Creando TitleRecordAudio
            val paramsTitleRecordAudio = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            paramsTitleRecordAudio.setMargins(0,0,0,5f.toDips().toInt())
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


            //Creating Record button
            val layoutRecordingAudio = LinearLayout(requireContext())
            val paramsLayoutRecording = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            val tagLayout = "ly${list.idQuestion}"
            layoutRecordingAudio.tag = tagLayout
            layoutRecordingAudio.orientation = LinearLayout.HORIZONTAL
            layoutRecordingAudio.layoutParams = paramsLayoutRecording
            layoutRecordingAudio.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_blue_button_reports)
            layoutRecordingAudio.gravity = Gravity.CENTER
            layoutRecordingAudio.setPadding(16f.toDips().toInt(),16f.toDips().toInt(),16f.toDips().toInt(),16f.toDips().toInt())
            layoutRecordingAudio.setOnClickListener {
                recordingAudio(tagLayoutContainer)
            }

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
            val tagRecordingtv = "tv${list.idQuestion}"
            tvRecording.tag = tag
            hashTextView[tagRecordingtv] = tvRecording
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
            val tagChronometer = "ch${list.idQuestion}"
            chronometer.tag = tag
            layoutRecordingAudio.addView(chronometer)
            //mutableHashMapTag[tagLayout] = Chr
            hashChronometer[tagChronometer] = chronometer

            //Creando grabacion - Clip
            val layoutShowAudioRecorded= LinearLayout(requireContext())
            val paramsLayoutAudioRecorded = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            paramsLayoutAudioRecorded.topMargin = 10f.toDips().toInt()
            val tagLayoutShowRecorded= "lyClip${list.idQuestion}"
            layoutShowAudioRecorded.tag = tagLayoutShowRecorded
            layoutShowAudioRecorded.orientation = LinearLayout.HORIZONTAL
            layoutShowAudioRecorded.layoutParams = paramsLayoutAudioRecorded
            layoutShowAudioRecorded.gravity = Gravity.CENTER
            hashLayoutShowRecord[tagLayoutShowRecorded] = layoutShowAudioRecorded

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
                playingAudioRecorded(tagLayoutContainer)
            }
            val tagRecordedText = "tvClip${list.idQuestion}"
            tvRecordedText.tag = tagRecordedText
            hashTextViewRecord[tagRecordedText] = tvRecordedText
            layoutShowAudioRecorded.addView(tvRecordedText)

            //Creating ImageView
            val imageViewClipClose= ImageView(requireContext())
            val paramsImageViewClipClose = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            imageViewClipClose.setImageResource(R.drawable.ic_close)
            imageViewClipClose.layoutParams = paramsImageViewClipClose
            val tagIvClose = "ivClip${list.idQuestion}"
            imageViewClipClose.tag = tagIvClose
            imageViewClipClose.setOnClickListener {
                deleteAudioRecorded(tagLayoutContainer)
            }
            hashImageVIewClipClose[tagIvClose] =imageViewClipClose
            layoutShowAudioRecorded.addView(imageViewClipClose)

            listTagRecordBtn.add(RecordAudio(tagLayout,tagRecordingtv,tagChronometer,tagLayoutShowRecorded,tagRecordedText,tagIvClose))
            //mutableHashMapTag[tagLayout] = RecordAudio(tagLayout,tagRecordingtv,tagChronometer,tagLayoutShowRecorded,tagRecordedText,tagIvClose)
            mutableHashMapTag[tagLayoutContainer] = RecordAudioViews(
                layoutTwoButtons,
                btnSelectedAudio,
                layoutRecordingAudio,
                tvRecording,
                chronometer,
                layoutShowAudioRecorded,
                tvRecordedText,
                imageViewClipClose)
            listSelectedAudioButton.add(btnSelectedAudio)
            //listRecordAudioButton.add(btnRecordAudio)
            linearLayoutContainer.addView(layoutTwoButtons)
            linearLayoutContainer.addView(layoutRecordingAudio)
            linearLayoutContainer.addView(layoutShowAudioRecorded)
        }
        linearLayoutContainer.layoutParams = params
        view.addView(linearLayoutContainer)
    }

    private fun deleteAudioRecorded(tag: String) {
        mutableHashMapTag[tag]?.let { recordAudio ->
            val tagLayoutContainer =recordAudio.layoutContainer.tag
            hashPathAudioRecorded[tagLayoutContainer.toString()] = ""
            recordAudio.layoutShowRecord.invisible()
        }
    }

    private fun Float.toDips() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)
    private fun recordingAudio(tag: String) {
        mutableHashMapTag[tag]?.let { recordAudio ->
            hashPathAudioRecorded[tag]?.let { Log.d("hashmap", it) }
            val tagLayoutContainer = recordAudio.layoutContainer.tag
            val previousRecord = hashPathAudioRecorded[tagLayoutContainer] ?: ""
            if(previousRecord == ""){
                val recording = dynamicAudioViewModel.recording
                if (recording == true) {
                    recordAudio.chrometerRecording.apply {
                        visibility = View.GONE
                        base = SystemClock.elapsedRealtime()
                        stop()
                    }
                    recordAudio.textViewRecording.visibility = View.VISIBLE
                    recordAudio.layoutShowRecord.visibility = View.VISIBLE
                    dynamicAudioViewModel.stopRecord()
                    hashPathAudioRecorded[tagLayoutContainer.toString()] = dynamicAudioViewModel.recordPath
                } else {
                    recordAudio.textViewRecording.invisible()
                    recordAudio.chrometerRecording.apply {
                        visibility = View.VISIBLE
                        base = SystemClock.elapsedRealtime()
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
                recordAudio.textViewShowRecord.invisible()
                try {
                    val playerRecorded = MediaPlayer().apply {
                        setDataSource(path)
                        prepare()
                        setOnCompletionListener {
                            recordAudio.chrometerRecording.apply {
                                visibility = View.GONE
                                base = SystemClock.elapsedRealtime()
                                stop()
                            }
                            recordAudio.layoutShowRecord.visibility = View.VISIBLE
                        }
                        start()
                        recordAudio.layoutShowRecord.invisible()
                        recordAudio.chrometerRecording.apply {
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
    @JvmName("getRealPathFromURI1")
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