package com.tawa.allinapp.features.reports.audiodinamico


import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.reports.audiodinamico.usecase.GetQuestionsAudioReport
import com.tawa.allinapp.features.reports.audiodinamico.usecase.GetStateAudioReport
import com.tawa.allinapp.features.reports.audiodinamico.usecase.SetAnswerPvAudio
import com.tawa.allinapp.models.Answer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

class DynamicAudioViewModel
@Inject constructor(
    private val getStateAudioReport: GetStateAudioReport,
    private val getQuestionsAudioReport: GetQuestionsAudioReport,
    private val setAnswerPvAudio: SetAnswerPvAudio
) : BaseViewModel() {

    private var _getStateReport = MutableLiveData<String>()
    val getStateReport : LiveData<String> get() = _getStateReport

    private var _getQuestions = MutableLiveData<List<Answer>>()
    val getQuestions : LiveData<List<Answer>> get() =  _getQuestions

    private var _setAnswers = MutableLiveData<Boolean>(false)
    val setAnswers: LiveData<Boolean> get() = _setAnswers

    private var recorder: MediaRecorder? = null
    private var playerRecorded: MediaPlayer? = null

    var recording: Boolean? = false
    var recordPath: String = ""

    fun playAudioRecord(path:String){
        playerRecorded = MediaPlayer().apply {
            try {
                setDataSource(path)
                prepare()
                setOnCompletionListener {
//                    _recording.value = false
//                    _textPlaying.value = false
                }
                start()
                recording = true
            } catch (e: IOException) {
                Log.e("PLAYING ERROR", " $e \n No se encuentra ael recordpat -> "+ recordPath.toString())
            }
        }
    }
    fun startRecord() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            val outputFolder = File(Environment.getExternalStorageDirectory().toString() + "/download/")
            val output = File(outputFolder.absolutePath + "/out" + Date().time + ".3gpp")

            Log.i("DIRECTORIO", output.absolutePath)
            recordPath = output.absolutePath
            setOutputFile(output.absolutePath)
            setMaxDuration(420000)
            try {
                prepare()
                start()
                recording = true
            } catch (e: IOException) {
                //prefs.audioRecorded = ""
                Log.e("RECORD", e.toString())
            }
        }
    }
    fun stopRecord() {
        recorder?.apply {
            stop()
            release()
            //val audio64 = convertImageFileToBase64(output!!)
            recording = false
        }
        recorder = null
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
    fun convertAudioSelectedTo64Format(path: String): String?{
        val outputSelected = File(path)
        return convertImageFileToBase64(outputSelected)
    }


    fun getStateReport(idReport: String, type: Int) {
        getStateAudioReport(GetStateAudioReport.Params(idReport)) {
            it.either(::handleFailure, ::handleGetStateReport)
        }
    }
    private fun handleGetStateReport(type: String) {
        this._getStateReport.value = type
    }

    fun getQuestions(idReport: String){
        getQuestionsAudioReport(GetQuestionsAudioReport.Params(idReport)){
            it.either(::handleFailure, ::handleGetQuestions)
        }
    }
    private fun handleGetQuestions(list: List<Answer>){
        this._getQuestions.value = list
    }

    fun setAnswerPvAudio(idAnswer: String,idQuestion: String,nameAnswer: String,img: String){
        setAnswerPvAudio(SetAnswerPvAudio.Params(idAnswer,idQuestion,nameAnswer,img)){
            it.either(::handleFailure, ::handleSetAnswerPvAudio)
        }
    }
    private fun handleSetAnswerPvAudio(success: Boolean){
        _setAnswers.value = false
    }
}