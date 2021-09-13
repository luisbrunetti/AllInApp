package com.tawa.allinapp.features.reports.audio

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
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

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == Activity.RESULT_OK){
            val data = activityResult.data?.extras?.get("data")
            Log.d("data",data.toString())
            val file = activityResult?.data?.getParcelableArrayListExtra<MediaFile>(FilePickerActivity.MEDIA_FILES)
            val mediaFile = file?.get(0)
            mediaFile?.let { path ->
                Log.d("data", mediaFile.toString())
                Log.d(TAG, "Obteniendo el path real -- >  ${mediaFile.uri.path}  ${mediaFile.path}" )

                audioSelectedPath = mediaFile.path
                audioSelected64 = audioViewModel.convertAudioSelectedTo64Format(audioSelectedPath).toString()
                audioSelectedName = mediaFile.name

                Log.d(TAG, audioSelected64.toString())
                binding.tvRecordSelected.text = mediaFile.name
                binding.lyRecordSelected.visibility = View.VISIBLE
            }
        }else{
            //notify(requireActivity(), R.string.error_get_file)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun getPathFromURI(uri: Uri?): String {
        var realPath = ""
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            @SuppressLint("Recycle") val cursor: Cursor? =
                activity?.contentResolver?.query(uri!!, proj, null, null, null)
            var column_index = 0
            val result = ""
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                realPath = cursor.getString(column_index)
            }
        } else if (Build.VERSION.SDK_INT < 19) {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursorLoader = CursorLoader(requireContext(), uri!!, proj, null, null, null)
            val cursor: Cursor? = cursorLoader.loadInBackground()
            if (cursor != null) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                realPath = cursor.getString(column_index)
            }
        } else {
            val wholeID = DocumentsContract.getDocumentId(uri)
            // Split at colon, use second item in the array
            val id = wholeID.split(":").toTypedArray()[0]
            Log.d(TAG, "getting array -- > ${wholeID.toString()}")
            Log.d(TAG, "getting array -- > ${wholeID.split(":").toTypedArray()[0]}")


            val column = arrayOf(MediaStore.Audio.Media.DATA)
            Log.d(TAG, " COLUMNN -- > ${column.toString()}")
            // where id is equal to
            val sel = MediaStore.Audio.Media._ID + "=?"
            val cursor: Cursor? = activity?.contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, column, sel, arrayOf(id), null)

            var columnIndex = 0
            if (cursor != null) {
                columnIndex = cursor.getColumnIndex(column[0])
                if (cursor.moveToFirst()) {
                    realPath = cursor.getString(columnIndex)
                }
                cursor.close()
            }
        }
        return realPath
    }

    fun getRealPathFromURI(context: Context?, contentUri: Uri?): String? {
        var path: String? = ""
        var cursor: Cursor? = activity?.contentResolver?.query(contentUri!!, null, null, null, null)
        cursor?.let {
            cursor!!.moveToFirst()
            var document_id = cursor!!.getString(0)
            Log.d("documentId", document_id.toString())
            document_id = document_id!!.substring(document_id.lastIndexOf(":") + 1)
            Log.d("documentId", document_id.toString())
            cursor!!.close()
            cursor = activity?.contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media._ID + " = ? ", arrayOf(document_id), null)
            Log.d("cursosr", cursor?.columnNames.toString() + "  "+ cursor.toString())
            cursor!!.moveToFirst()
            val getColumnName = cursor?.getColumnIndex(MediaStore.Audio.Media.DATA)
            Log.d("GETCXolumn Name", getColumnName.toString())
            path = cursor!!.getString(getColumnName!!)
            cursor!!.close()
        }
        return path
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
                    //audio64 = ""
                    //audioPath = ""
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
                    //val timeLimitString = getMiliSeoncdsOnFormat(audioLimit)
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
                audioViewModel.setReadyAnswers(idQuestion, nameQuestion, idAnswer, audio64, "") // Work // Poner validación
                checkListViewModel.setAnswerPv(idAnswer, idQuestion, audio64, "")
                //Actualizando reportesa
                audioViewModel.updateStateReport(idReport, "En proceso", "Terminado")
                checkListViewModel.updateReportPv(idReport, "En proceso", "Terminado", Calendar.getInstance().toInstant().toString(), latitude, longitude)

                //Guardando audio
                if (audioReportSaved != null) {
                    if (audio64 != ""){
                        Log.d("AudioReport","Reportguardado  Actualizando audio 64 -- -> audio")
                        audioViewModel.updateAudioReport(AudioReport(idPv, idUser, audioSelected64, audioSelectedName, audioSelectedPath, audio64, audioPath, audio64))
                    }
                    else if (audioSelected64 != "") {
                        Log.d("AudioReport","Report guardado  Actualizando audioSelected64 -- -> audio")
                        audioViewModel.updateAudioReport(AudioReport(idPv, idUser, audioSelected64, audioSelectedName,audioSelectedPath, audio64, audioPath, audioSelected64))
                    }
                } else {
                    if (audio64 != "") {
                        Log.d("AudioReport","Report no guardado  Creando audio64 -- -> audio")
                        audioViewModel.saveReport(AudioReport(idPv, idUser, audioSelected64, audioSelectedName,audioSelectedPath, audio64, audioPath, audio64))
                    }
                    else if (audioSelected64 != "") {
                        Log.d("AudioReport","Report no guardado  Creando audioSelected64 -- -> audio")
                        audioViewModel.saveReport(
                            AudioReport(idPv, idUser, audioSelected64, audioSelectedName, audioSelectedPath, audio64, audioPath, audioSelected64)
                        )
                    }
                }
                audioViewModel.syncStandardReports(idReport, latitude, longitude)

                activity?.onBackPressed()
            }

            override fun onBack() {
                confirmationDialog.dismiss()
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
            //audioViewModel.setReadyAnswers(idQuestion, nameQuestion, idAnswer, audio64, "") // Aqui faltaria poner en la estructura la c
            //checkListViewModel.setAnswerPv(idAnswer, idQuestion, audio64, "") // Falta cheuear lo de Answer (Ingrese Audio

            checkListViewModel.updateReportPv(
                idReport,
                "En proceso",
                "Borrador",
                Calendar.getInstance().toInstant().toString(),
                latitude,
                longitude
            )
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
                showFilePicker()
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


    private fun getFilename(uri: Uri): String? {
        val cursor = activity?.contentResolver?.query(uri, null, null, null, null)
        var filename: String? = null

        cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)?.let { nameIndex ->
            cursor.moveToFirst()

            filename = cursor.getString(nameIndex)
            cursor.close()
        }

        return filename
    }


    private fun setUpBinding() {
        binding.viewModel = audioViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    private fun showFilePicker() {

        val intent = Intent(requireActivity(),FilePickerActivity::class.java)
        intent.putExtra(FilePickerActivity.CONFIGS, Configurations.Builder()
            .setCheckPermission(true)
            .setShowAudios(true)
            .setShowFiles(true)
            .setShowImages(false)
            .setShowVideos(false)
            .setMaxSelection(1)
            .setSkipZeroSizeFiles(false)
            .setSuffixes("mp3","3gpp")
            .build())
        //intent.type = "audio/*"
        /*
        try {
            startActivityForResult(intent, REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace();
            Log.e("tag", "No activity")
        }*/
        responseLauncher.launch(intent)


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
}


