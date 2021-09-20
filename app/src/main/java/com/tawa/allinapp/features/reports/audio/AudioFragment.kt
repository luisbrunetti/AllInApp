package com.tawa.allinapp.features.reports.audio

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.loader.content.CursorLoader
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.ConditionalDialogFragment
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.*
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentAudioBinding
import com.tawa.allinapp.features.reports.standard.CheckListViewModel
import com.tawa.allinapp.features.reports.standard.ConfirmSyncDialogFragment
import com.tawa.allinapp.models.AudioReport
import java.io.File
import java.util.*


class AudioFragment : BaseFragment() {

    private lateinit var binding: FragmentAudioBinding
    private lateinit var audioViewModel: AudioViewModel
    private lateinit var checkListViewModel: CheckListViewModel
    private var idQuestion = ""
    private var nameQuestion = ""
    private var idAnswer = ""
    private var idUser = ""
    private var nameAnswer = ""
    private var idReport = ""
    private var idPv = ""
    private var state = ""
    private var audioLimit: Int = 1000 * 60 * 7 // 7 minutos
    private val confirmationDialog: ConfirmSyncDialogFragment = ConfirmSyncDialogFragment()

    private var audio64 = ""
    private var audioPath = ""
    private var audioSelected64: String = ""
    private var audioSelectedName: String = ""
    private var audioSelectedPath: String = ""
    private var audioReportSaved: AudioReport? = null

    companion object {
        val REQUEST_CODE = 200
        val TAG = "AudioFragment"
    }

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val uri = uri
            val realPath = getRealPathFromURI(requireContext(), uri)
            realPath.let { realPath->
                audioSelectedPath = realPath.toString()
                audioSelected64 = audioViewModel.convertAudioSelectedTo64Format(audioSelectedPath).toString()

                audioSelectedName = getFilename(uri)

                binding.tvRecordSelected.text = audioSelectedName
                binding.lyRecordSelected.visibility = View.VISIBLE
                Log.d("realPath", realPath.toString())
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        checkPermissions()
        getLastLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioBinding.inflate(inflater)
        arguments?.getString("id").toString().also { idRep ->
            idReport = idRep
        }
        arguments?.getString("idPv").toString().also { idPv ->
            this.idPv = idPv
        }
        arguments?.getString("idUser").toString().also { idUser ->
            this.idUser = idUser
        }
        arguments?.getString("state").toString().also { state ->
            this.state = state
            if (state == "Enviado") disableComponents()
        }
        Log.d("Arguments", "$idReport $idPv $idUser $state")
        checkListViewModel = viewModel(viewModelFactory) {

        }

        audioViewModel = viewModel(viewModelFactory) {
            observe(getAudiosReport, {
                if(it?.idUser != ""){
                    it?.let { info ->
                        //Log.d("listAudio", it.toString())
                        audioReportSaved = info
                        audio64 = info.record
                        audioPath = info.recordPath
                        audioViewModel.setRecordPath(audioPath)

                        audioSelected64 = info.selected
                        audioSelectedName = info.selectedName
                        audioSelectedPath = info.selectedPath
                        //////Vistas
                        if (audioSelectedName != "") {
                            binding.lyRecordSelected.visible()
                            binding.tvRecordSelected.text = info.selectedName
                        } else {
                            binding.lyRecordSelected.invisible()
                        }

                        if (audioPath != "") {
                            binding.rvAudioRecord.visible()
                        } else {
                            binding.rvAudioRecord.invisible()
                        }

                        audioViewModel.setFileString(audio64)
                    }
                }
            })
            observe(updateAudioReports) {
                if (it == true) {
                    notify(requireActivity(), R.string.successful_update_audio)
                }
            }
            observe(fileString, {
                it?.let {
                    if (it != "") {
                        audio64 = it
                        audioPath = audioViewModel.getAudioRecordPath()
                        binding.rvAudioRecord.visible()
                        //binding.tvRecordSelected.text = getSelected()
                    } else
                        binding.rvAudioRecord.invisible()
                }
            })
            observe(fileSelectedString, {
                it?.let {
                    if (it != "") {
                        audioSelected64 = it
                        binding.lyRecordSelected.visible()
                    } else {
                        audioSelected64 = ""
                        binding.lyRecordSelected.invisible()
                    }
                }
            })
            observe(recording, {
                it?.let {
                    if (it) {
                        val timeLimitString = getMiliSeoncdsOnFormat(audioLimit)
                        binding.chronometer.base = SystemClock.elapsedRealtime()
                        binding.chronometer.setOnChronometerTickListener { chronometer ->
                            Log.d(
                                "LogTimer",
                                " ${SystemClock.elapsedRealtime()} isEqual : $timeLimitString - ${chronometer.text.trim()}"
                            )
                            if (chronometer.text.trim() == timeLimitString) {
                                binding.chronometer.onChronometerTickListener = null
                                binding.chronometer.stop()
                                binding.chronometer.base = SystemClock.elapsedRealtime()
                                notify(activity, R.string.error_audio_limit)
                                stopRecordingByLimit()
                            }
                        }
                        binding.chronometer.start()
                        binding.rvAudioRecord.invisible()
                    } else {
                        binding.chronometer.base = SystemClock.elapsedRealtime()
                        binding.chronometer.stop()
                        if (audio64 != "" && audioPath != "") binding.rvAudioRecord.visible()
                    }
                }
            })
            observe(successAudioQuestions, {
                it?.let {
                    for (audio in it) {
                        idQuestion = audio.id
                        nameQuestion = audio.questionName
                        audioViewModel.getAnswersAudio(audio.id)
                    }
                }
            })
            observe(syncAudioReport, {
                it?.let {
                    if(it){
                        notify(requireActivity(), R.string.success_sync_audio)
                    }
                }
            })
            observe(answersAudio, {
                it?.let {
                    for (ans in it) {
                        idAnswer = ans.id
                        nameAnswer = ans.answerName
                    }
                }
            })
            failure(failure, {
                it?.let {
                    hideProgressDialog()
                    when (it) {
                        is Failure.NetworkConnection -> MessageDialogFragment.newInstance(
                            getString(
                                R.string.error_network
                            )
                        ).show(childFragmentManager, "dialog")
                        is Failure.ServerError -> MessageDialogFragment.newInstance(getString(R.string.error_network))
                            .show(childFragmentManager, "dialog")
                        else -> MessageDialogFragment.newInstance(getString(R.string.error_unknown))
                            .show(childFragmentManager, "dialog")
                    }
                }
            })
            observe(textPlaying, {
                if (it == true) binding.tvPlaying.visibility = View.VISIBLE
                else binding.tvPlaying.visibility = View.GONE
            })
            observe(playingSelected,{
                if(it == true){
                    binding.lyPlayingAudioSelected.visibility = View.VISIBLE
                }else{
                    binding.lyPlayingAudioSelected.visibility = View.GONE
                }
            })
            observe(textPlayingSelected,{
                if(it == true){

                    binding.chronometerSelectAudio.base = SystemClock.elapsedRealtime()
                    binding.chronometerSelectAudio.start()
                    binding.lyRecordSelected.visibility = View.GONE
                }
                else{
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.chronometer.stop()
                    if(audioSelectedPath != "" && audioSelected64 != "") binding.lyRecordSelected.visible()
                    enableComponentsRecord()
                }
            })
            observe(displayMessage, {
                it?.let {
                    if (it) {
                        val conditionalFragment = ConditionalDialogFragment.newInstance(
                            title = "El audio anterior será borrado",
                            message = "¿Desea sobrescribir el audio que ya se grabó?",
                            icon = R.drawable.ic_warning,
                            btn1 = "Sí",
                            btn2 = "No"
                        )
                        conditionalFragment.listener = object : ConditionalDialogFragment.Callback {
                            override fun onAccept() {
                                audioViewModel.reRecordAudio()
                            }
                        }
                        conditionalFragment.show(childFragmentManager, "dialog")
                    }
                }
            })
        }

        binding.tvRecord.setOnClickListener { audioViewModel.doSelectAudio(audioPath) }
        binding.ivRecordSelectedDelete.setOnClickListener {
            binding.lyRecordSelected.invisible()
            audioSelected64 = ""
            audioSelectedName = ""
            audioSelectedPath = ""
            audioViewModel.clearAudioSelected()
        }
        binding.ivClose.setOnClickListener {
            binding.rvAudioRecord.invisible()
            audioPath = ""
            audio64 = ""
            audioViewModel.clearAudioRecorded()
        }
        confirmationDialog.listener = object : ConfirmSyncDialogFragment.Callback {
            override fun onClick() {
                var audioSend = ""
                if(audio64 != "") audioSend = audio64
                else if(audioSelected64 != "") audioSend = audioSelected64

                //Set Ready Answers
                audioViewModel.setReadyAnswers(idQuestion, nameQuestion, idAnswer, audioSend, "") // Work // Poner validación
                checkListViewModel.setAnswerPv(idAnswer, idQuestion, audioSend, "")

                //Actualizando reportesa
                audioViewModel.updateStateReport(idReport, "En proceso", "Terminado")
                checkListViewModel.updateReportPv(idReport, "En proceso", "Terminado", Calendar.getInstance().toInstant().toString(), latitude, longitude)

                //Guardando audio
                if(audioReportSaved != null) audioViewModel.updateAudioReport(AudioReport(idPv, idUser, audioSelected64, audioSelectedName, audioSelectedPath, audio64, audioPath, audioSend))
                else audioViewModel.saveReport(AudioReport(idPv, idUser, audioSelected64, audioSelectedName,audioSelectedPath, audio64, audioPath, audioSend))
                audioViewModel.syncStandardReports(idReport, latitude, longitude)

                activity?.onBackPressed()
            }

            override fun onBack() {
                var audioSend = ""
                if(audio64 != "") audioSend = audio64
                else if(audioSelected64 != "") audioSend = audioSelected64

                //Set Ready Answers
                audioViewModel.setReadyAnswers(idQuestion, nameQuestion, idAnswer, audioSend, "") // Work // Poner validación
                checkListViewModel.setAnswerPv(idAnswer, idQuestion, audioSend, "")

                //Actualizando reportesa
                audioViewModel.updateStateReport(idReport, "En proceso", "Terminado")
                checkListViewModel.updateReportPv(idReport, "En proceso", "Terminado", Calendar.getInstance().toInstant().toString(), latitude, longitude)

                //Guardando audio
                if(audioReportSaved != null) audioViewModel.updateAudioReport(AudioReport(idPv, idUser, audioSelected64, audioSelectedName, audioSelectedPath, audio64, audioPath, audioSend))
                else audioViewModel.saveReport(AudioReport(idPv, idUser, audioSelected64, audioSelectedName,audioSelectedPath, audio64, audioPath, audioSend))

                confirmationDialog.dismiss()
                activity?.onBackPressed()
            }
        }
        binding.btSavePictures.setOnClickListener {
            if (audio64 != "" && audioSelected64 != "") {
                notify(activity, R.string.only_one_audio)
            } else {
                if (audio64 == "" && audioSelected64 == "") notify(activity, R.string.no_audios)
                else confirmationDialog.show(childFragmentManager, "dialog")
            }
        }
        binding.btErraser.setOnClickListener {
            val report = AudioReport(idPv, idUser, audioSelected64, audioSelectedName,audioSelectedPath, audio64, audioPath, "NO CONFIRMED")
            Log.d(TAG, "Audio report que se guardará " + report.toModel())
            checkListViewModel.setAnswerPv(idAnswer, idQuestion, audio64, "") // Falta chequear lo de Answer Ingrese Audio

            checkListViewModel.updateReportPv(idReport, "En proceso", "Borrador", Calendar.getInstance().toInstant().toString(), latitude, longitude)
            audioViewModel.updateStateReport(idReport, "En proceso", "Borrador")

            if (audioReportSaved != null) {
                Log.d(TAG, "Actualizando Audio")
                audioViewModel.updateAudioReport(report)
            } else {
                Log.d(TAG, "Guardando Audio")
                audioViewModel.saveReport(report)
            }

            activity?.onBackPressed()
        }

        binding.btTakeAudioSelect.setOnClickListener {
            if(binding.lyPlayingAudioSelected.visibility == View.VISIBLE){
                audioViewModel.stopAudioSelectedPlaying()
            }else{
                responseLauncher.launch("audio/*")
            }
        }
        binding.lyRecordSelected.setOnClickListener { if(audioSelectedName != ""){
            disableComponentsRecord()
            audioViewModel.doSelectedAudio(audioSelectedPath)
        } }
        audioViewModel.setAudioLimit(audioLimit)
        audioViewModel.getAudioQuestions()

        //Room
        audioViewModel.getAudioReport(idPv, idUser)

        return binding.root
    }
    private fun disableComponentsRecord(){
        binding.btTakeAudioRecord.isEnabled= false
        binding.tvPlaying.isEnabled = false

    }
    private fun enableComponentsRecord(){
        binding.btTakeAudioRecord.isEnabled= true
        binding.tvPlaying.isEnabled = true
    }
    private fun disableComponents() {
        binding.btTakeAudioSelect.apply {
            isEnabled = false
            alpha = 0.5F
        }

        binding.btTakeAudioRecord.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.btErraser.apply {
            isEnabled = false
            alpha = 0.5F
        }
        binding.btSavePictures.apply {
            isEnabled = false
            alpha = 0.5f
        }
        binding.ivRecordSelectedDelete.apply {
            isEnabled = false
            alpha = 0.5f
        }
        binding.ivClose.apply {
            isEnabled = false
            alpha = 0.5f
        }
        binding.lyRecordSelected.apply {
            isEnabled = false
            alpha = 0.5f
        }
        binding.rvAudioRecord.apply {
            isEnabled = false
            alpha = 0.5f
        }
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


    private fun setUpBinding() {
        binding.viewModel = audioViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
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

    override fun onDestroy() {
        super.onDestroy()
        audioViewModel.stopAudioSelectedPlaying()
        audioViewModel.stopAudioPlaying()
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


