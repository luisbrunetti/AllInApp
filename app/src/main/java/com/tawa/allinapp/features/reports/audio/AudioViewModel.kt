package com.tawa.allinapp.features.reports.audio

import android.Manifest
import android.app.Application
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import android.util.Base64OutputStream
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.models.AudioReport
import java.io.ByteArrayOutputStream
import java.io.File
import android.util.Base64
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.features.reports.standard.GetAnswers
import com.tawa.allinapp.features.reports.standard.SetReadyAnswers
import com.tawa.allinapp.features.reports.standard.UpdateStateReport
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import java.io.IOException
import java.util.*
import javax.inject.Inject

class AudioViewModel
@Inject constructor(
    private val setAudioReport: SetAudioReport,
    private val getAudioQuestion: GetAudioQuestion,
    private val getAnswers: GetAnswers,
    private  val setReadyAnswers: SetReadyAnswers,
    private  val updateStateReport: UpdateStateReport
): BaseViewModel(){

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var fileName: String? = ""
    private var output: File? = null

    private val _recording = MutableLiveData(false)
    val recording = _recording

    private val _timeRecord = MutableLiveData("0:00")
    val timeRecord = _timeRecord

    private val _record = MutableLiveData("")
    val record = _record

    private val _fileString = MutableLiveData("")
    val fileString = _fileString

    private val _successRecord = MutableLiveData(false)
    val successRecord: LiveData<Boolean>
        get()= _successRecord

    private val _successAudio = MutableLiveData<List<Question>>()
    val successAudio: LiveData<List<Question>>
        get()= _successAudio

    private val _answersAudio = MutableLiveData<List<Answer>>()
    val answersAudio: LiveData<List<Answer>>
        get()= _answersAudio

    private val _successReadyAnswers = MutableLiveData<Boolean>(false)
    val successReadyAnswers: LiveData<Boolean>
        get()= _successReadyAnswers

    private val _updateReportState = MutableLiveData<Boolean>(false)
    val updateReportState: LiveData<Boolean>
        get()= _updateReportState

    private val _displayMessage = MutableLiveData<Boolean>(false)
    val displayMessage : LiveData<Boolean> get() = _displayMessage

    fun doRecordAudio(){
        if(record.value != ""){
            if (_recording.value==true)
                stopRecord()
            else
                startRecord()
        }else{
            _displayMessage.value = true
        }
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(_record.value)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("PLAYING ERROR", "prepare() failed")
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

    private fun startRecord(){
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            val outputFolder = File(Environment.getExternalStorageDirectory().toString() + "/download/")
            output = File(outputFolder.absolutePath +"/out" + Date().time + ".3gpp")
            Log.i("DIRECTORIO", output!!.absolutePath)
            _record.value = output!!.absolutePath

            setOutputFile(_record.value)
            setMaxDuration(7000)

            try {
                prepare()
                start()
                _recording.value = true
            } catch (e: IOException) {
                Log.e("RECORD", e.toString())
            }
        }
    }

    private fun stopRecord() {
        recorder?.apply {
            stop()
            release()
            _fileString.value = convertImageFileToBase64(output!!)
            _recording.value=false
        }
        recorder = null
    }

    fun doSelectAudio(){
        startPlaying()
    }

    fun saveReport(report: AudioReport) = setAudioReport(SetAudioReport.Params(report)) { it.either(::handleFailure, ::handleReports) }

    private fun handleReports(reports: Boolean) {
        _successRecord.value = reports
    }

    fun getAudioQuestions() = getAudioQuestion(UseCase.None()) { it.either(::handleFailure, ::handleAudioQuestions) }

    private fun handleAudioQuestions(audioQuestions: List<Question>) {
        _successAudio.value = audioQuestions
    }

    fun getAnswersAudio(idQuestion:String) = getAnswers(GetAnswers.Params(idQuestion)) {
        it.either(::handleFailure, ::handleAnswersPhoto) }

    private fun handleAnswersPhoto(answers : List<Answer>) {
        _answersAudio.value = answers
    }

    fun setReadyAnswers(idQuestion: String,nameQuestion: String,idAnswer:String,nameAnswer: String,img:String) {
        setReadyAnswers(SetReadyAnswers.Params(0,idQuestion,nameQuestion,idAnswer,nameAnswer,img)) {
            it.either(::handleFailure, ::handleReadyAnswers)
        }
    }

    private fun handleReadyAnswers(success: Boolean) {
        this._successReadyAnswers.value = success
    }

    fun updateStateReport(idReport:String,state:String,type:String) { updateStateReport(UpdateStateReport.Params(idReport,state,type)) {
        it.either(::handleFailure, ::handleUpdateStateReport)
    }
    }
    private fun handleUpdateStateReport(success: Boolean) {
        this._updateReportState.value = success
    }
}