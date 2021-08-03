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
import java.io.IOException
import java.util.*
import javax.inject.Inject

class AudioViewModel
@Inject constructor(
    private val setAudioReport: SetAudioReport,
): BaseViewModel(){

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var fileName: String? = ""
    private var output: File? = null

    private val _recording = MutableLiveData(false)
    val recording: LiveData<Boolean>
        get()= _recording

    private val _record = MutableLiveData("")
    val record = _record

    private val _file = MutableLiveData("")
    val file = _file

    private val _successRecord = MutableLiveData(false)
    val successRecord: LiveData<Boolean>
        get()= _successRecord

    fun doRecordAudio(){
        if (_recording.value==true)
            stopRecord()
        else
            startRecord()
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

            val outputFolder = File(Environment.getExternalStorageDirectory().toString() + "/documents/")
            output = File(outputFolder.absolutePath +"out" + Date().time + ".3gpp")
            Log.i("DIRECTORIO", output!!.absolutePath)
            _record.value = output!!.absolutePath

            setOutputFile(_record.value)
            setMaxDuration(3000)

            try {
                prepare()
                start()
                _recording.value = true
            } catch (e: IOException) {
                Log.e("RECORD", "prepare() failed")
            }
        }
    }

    private fun stopRecord() {
        recorder?.apply {
            stop()
            release()
            _file.value = convertImageFileToBase64(output!!)
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
}