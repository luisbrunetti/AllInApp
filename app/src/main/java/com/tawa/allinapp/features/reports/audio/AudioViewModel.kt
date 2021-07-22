package com.tawa.allinapp.features.reports.audio

import android.Manifest
import android.app.Application
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
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
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

class AudioViewModel
@Inject constructor(
    private val setAudioReport: SetAudioReport,
): BaseViewModel(){

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var fileName: String = ""

    private val _recording = MutableLiveData(false)
    val recording: LiveData<Boolean>
        get()= _recording

    private val _record = MutableLiveData("")
    val record: LiveData<String>
        get()= _record

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
                setDataSource(fileName)
                prepare()
                start()
                _record.value = fileName
            } catch (e: IOException) {
                Log.e("PLAYING ERROR", "prepare() failed")
            }
        }
    }

    private fun startRecord(){
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            val outputFolder = File(Environment.getExternalStorageDirectory().toString() + "/audios/")
            val output = File(outputFolder.absolutePath +"out" + Date().time + ".3gpp")
            Log.i("DIRECTORIO", output.absolutePath)
            fileName = output.absolutePath

            setOutputFile(fileName)
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
            _recording.value=false
        }
        recorder = null
    }

    private fun checkRecordPermissions(){
        Dexter.withActivity(HomeActivity())
            .withPermissions(Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) { startRecord() }
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    fun doSelectAudio(){
        //startPlaying()
    }

    fun saveReport(report: AudioReport) = setAudioReport(SetAudioReport.Params(report)) { it.either(::handleFailure, ::handleReports) }

    private fun handleReports(reports: Boolean) {
        _successRecord.value = reports
    }
}