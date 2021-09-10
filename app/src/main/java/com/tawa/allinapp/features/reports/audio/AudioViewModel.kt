package com.tawa.allinapp.features.reports.audio

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.features.init.usecase.SyncAudio
import com.tawa.allinapp.features.init.usecase.SyncStandardReports
import com.tawa.allinapp.features.reports.standard.GetAnswers
import com.tawa.allinapp.features.reports.standard.SetReadyAnswers
import com.tawa.allinapp.features.reports.standard.UpdateStateReport
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.Question
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

class AudioViewModel
@Inject constructor(
    private val setAudioReport: SetAudioReport,
    private val getAudioQuestion: GetAudioQuestion,
  //  private val getAudioReport: GetAudioReport,
    private val getAnswers: GetAnswers,
    private val setReadyAnswers: SetReadyAnswers,
    private val updateStateReport: UpdateStateReport,
    private val syncAudio: SyncAudio,
    private val prefs: Prefs,
    private val syncStandardReports: SyncStandardReports
) : BaseViewModel() {

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var output: File? = null
    private var audioLimit: Int = 0
    private var playingAudio: Boolean = false

    private var recordPath: String = ""
    private var recordName: String = ""
    private var selectedName: String = ""
    private var selectedPath: String = ""

    private val _recording = MutableLiveData(false)
    val recording = _recording

    private val _timeRecord = MutableLiveData("0:00")
    val timeRecord = _timeRecord

    //Files Names

    private val _fileSelectedString = MutableLiveData("")
    val fileSelectedString :LiveData<String> = _fileSelectedString

    private val _fileString = MutableLiveData("")
    val fileString = _fileString

    //SyncAudio
    private val _syncAudioReport = MutableLiveData(false)
    val syncAudioReport: LiveData<Boolean> = _syncAudioReport

    // Guardano en memoria
    private val _successRecord = MutableLiveData(false)



    val successRecord: LiveData<Boolean>
        get() = _successRecord

    private val _successAudio = MutableLiveData<List<Question>>()
    val successAudio: LiveData<List<Question>>
        get() = _successAudio

    private val _answersAudio = MutableLiveData<List<Answer>>()
    val answersAudio: LiveData<List<Answer>>
        get() = _answersAudio

    private val _successReadyAnswers = MutableLiveData<Boolean>(false)
    val successReadyAnswers: LiveData<Boolean>
        get() = _successReadyAnswers

    private val _updateReportState = MutableLiveData<Boolean>(false)
    val updateReportState: LiveData<Boolean>
        get() = _updateReportState

    private val _displayMessage = MutableLiveData<Boolean>(false)
    val displayMessage: LiveData<Boolean> get() = _displayMessage

    private val _textPlaying = MutableLiveData<Boolean>(false)
    val textPlaying: LiveData<Boolean> get() = _textPlaying

    fun doRecordAudio() {
        if (_recording.value == true) {
            player?.let {
                stopAudioPlaying()
            }
            stopRecord()
        } else {
            if (_fileString.value == "") {
                startRecord()
            } else {
                _displayMessage.value = true
            }
        }
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(recordPath)
                prepare()
                setOnCompletionListener {
                    _recording.value = false
                    _textPlaying.value = false
                }
                start()
                _textPlaying.value = true
                playingAudio = true
                _recording.value = true
            } catch (e: IOException) {
                Log.e("PLAYING ERROR", e.toString())
            }
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

    private fun startRecord() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            val outputFolder = File(Environment.getExternalStorageDirectory().toString() + "/download/")
            output = File(outputFolder.absolutePath + "/out" + Date().time + ".3gpp")

            Log.i("DIRECTORIO", output!!.absolutePath)
            recordPath = output!!.absolutePath
            val directory = output!!.absolutePath
            setOutputFile(directory)
            setMaxDuration(audioLimit)
            try {
                prepare()
                start()
                _recording.value = true
            } catch (e: IOException) {
                prefs.audioRecorded = ""
                Log.e("RECORD", e.toString())
            }
        }
    }

    private fun stopRecord() {
        recorder?.apply {
            stop()
            release()
            val audio64 = convertImageFileToBase64(output!!)
            _fileString.value = audio64
            prefs.audioRecorded = audio64
            prefs.audioRecordedPath = recordPath
            _recording.value = false
        }
        recorder = null
    }
    private fun stopAudioPlaying() {
        player?.let {
            player?.apply {
                _textPlaying.value = false
                stop()
                release()
                _recording.value = false
            }
        }
        player = null
    }


    fun doSelectAudio() {
        startPlaying()
    }

    fun saveReport(report: AudioReport) = setAudioReport(SetAudioReport.Params(report)) {
        it.either(
            ::handleFailure,
            ::handleReports
        )
    }


    private fun handleReports(reports: Boolean) {
        _successRecord.value = reports
    }

    fun getAudioQuestions() = getAudioQuestion(UseCase.None()) {
        it.either(::handleFailure, ::handleAudioQuestions)
    }

    private fun handleAudioQuestions(audioQuestions: List<Question>) {
        Log.d("audioQuestions", audioQuestions.toString())
        _successAudio.value = audioQuestions
    }

    fun getAnswersAudio(idQuestion: String) = getAnswers(GetAnswers.Params(idQuestion)) {
        it.either(::handleFailure, ::handleAnswersPhoto)
    }

    private fun handleAnswersPhoto(answers: List<Answer>) {
        _answersAudio.value = answers
    }

    fun setReadyAnswers(
        idQuestion: String,
        nameQuestion: String,
        idAnswer: String,
        nameAnswer: String,
        img: String
    ) {
        setReadyAnswers(
            SetReadyAnswers.Params(
                0,
                idQuestion,
                nameQuestion,
                idAnswer,
                nameAnswer,
                img
            )
        ) {
            it.either(::handleFailure, ::handleReadyAnswers)
        }
    }

    private fun handleReadyAnswers(success: Boolean) {
        this._successReadyAnswers.value = success
    }

    fun updateStateReport(idReport: String, state: String, type: String) {
        updateStateReport(UpdateStateReport.Params(idReport, state, type)) {
            it.either(::handleFailure, ::handleUpdateStateReport)
        }
    }

    private fun handleUpdateStateReport(success: Boolean) {
        this._updateReportState.value = success
    }

    fun saveSelectedAudio(audioSelected64: String,audioSelectedPath: String){
        prefs.audioSelectedPath = audioSelectedPath
        prefs.audioSelected = audioSelected64
    }

    fun existPreviousRecord() {
        recordPath = prefs.audioRecordedPath.toString()
        _fileString.value = prefs.audioRecorded
    }

    fun existPreviousSelectedAudio(){
        _fileSelectedString.value = prefs.audioSelected
        selectedPath = prefs.audioSelectedPath.toString()
    }

    fun getSelectedPath() : String = prefs.audioSelectedPath.toString()
    fun getSelected() : String = prefs.audioSelected.toString()
    fun getRecordedPath() : String = prefs.audioRecordedPath.toString()
    fun getRecord(): String = prefs.audioRecorded.toString()

    fun reRecordAudio() {
        this._displayMessage.value = false
        this._fileString.value = ""
        recordPath = ""
        doRecordAudio()
    }

    fun setAudioLimit(limit: Int) {
        audioLimit = limit
    }

    fun stopRecordingByLimit() {
        stopRecord()
    }

    fun clearAudioRecorded() {
        prefs.audioRecorded = ""
        prefs.audioRecordedPath = ""
        this._fileString.value = ""
        this.recordPath = ""
    }

    fun clearAudioSelected(){
        prefs.audioSelected = ""
        prefs.audioSelectedPath = ""
        _fileSelectedString.value = ""
        selectedPath = ""
    }
    fun convertAudioSelectedTo64Format(path: String): String?{
        val outputSelected = File(path)
        return convertImageFileToBase64(outputSelected)
    }

    fun syncStandardReports(idReport: String, latitude:String,longitude: String) =
        syncStandardReports(SyncStandardReports.Params(idReport,latitude,longitude))
        { it.either(::handleFailure, ::handleSyncAudioReport) }

    /*fun getAudioReport() = getAudioReport(UseCase.None()){

    }*/

    private fun handleSyncAudioReport(success: Boolean){
        this._syncAudioReport.value = success
    }

}