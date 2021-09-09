package com.tawa.allinapp.features.reports.audio

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.FileUtils
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.skydoves.balloon.createBalloon
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
    private var nameAnswer = ""
    private var audio64 = ""
    private var idReport = ""
    private var audioLimit: Int = 1000*60*7 // 7 minutos
    private val confirmationDialog: ConfirmSyncDialogFragment = ConfirmSyncDialogFragment()
    private var audioSelected : String? = null
    companion object val REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        checkPermissions()
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

        checkListViewModel = viewModel(viewModelFactory) {

        }

        audioViewModel = viewModel(viewModelFactory) {
            observe(fileString, {
                it?.let {
                    if (it.isNotEmpty()) {
                        audio64 = it
                        binding.rvAudioRecord.visible()
                    } else
                        binding.rvAudioRecord.invisible()
                }
            })
            observe(recording, {
                it?.let {
                    if (it) {
                        val timeLimitString = getMiliSeoncdsOnFormat(audioLimit)
                        binding.chronometer.base = SystemClock.elapsedRealtime()
                        binding.chronometer.setOnChronometerTickListener { chronometer ->
                            Log.d("LogTimer", " ${SystemClock.elapsedRealtime()} isEqual : $timeLimitString - ${chronometer.text.trim()}")
                            if(chronometer.text.trim() == timeLimitString){
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
                        if(audioViewModel.existPreviousRecord() != "") binding.rvAudioRecord.visible()
                    }
                }
            })
            observe(successAudio, {
                it?.let {
                    for (audio in it) {
                        idQuestion = audio.id
                        nameQuestion = audio.questionName
                        audioViewModel.getAnswersAudio(audio.id)
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
            observe(textPlaying,{
                if(it == true) binding.tvPlaying.visibility= View.VISIBLE
                else binding.tvPlaying.visibility= View.GONE
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
                        conditionalFragment.listener = object : ConditionalDialogFragment.Callback{
                            override fun onAccept() {
                                audioViewModel.reRecordAudio()
                            }
                        }
                        conditionalFragment.show(childFragmentManager, "dialog")

                    }
                }
            })
        }
        visibilityAudioRecorded()
        binding.ivRecordSelectedDelete.setOnClickListener { binding.lyRecordSelected.visibility = View.GONE }
        binding.ivClose.setOnClickListener {
            binding.rvAudioRecord.invisible()
            audioViewModel.clearAudioRecorded()
        }
        confirmationDialog.listener = object : ConfirmSyncDialogFragment.Callback {
            override fun onClick() {
                getLastLocation()
                audioViewModel.setReadyAnswers(idQuestion, nameQuestion, idAnswer, audio64, "") // Work
                audioViewModel.updateStateReport(idReport, "En proceso", "Terminado")
                checkListViewModel.updateReportPv(
                    idReport,
                    "En proceso",
                    "Terminado",
                    Calendar.getInstance().toInstant().toString(),
                    latitude,
                    longitude
                )
                checkListViewModel.setAnswerPv(idAnswer, idQuestion, nameAnswer, "")
                activity?.onBackPressed()
            }

            override fun onBack() {
                getLastLocation()
                audioViewModel.setReadyAnswers(idQuestion, nameQuestion, idAnswer, audio64, "") // Work
                audioViewModel.updateStateReport(idReport, "En proceso", "Terminado")
                checkListViewModel.updateReportPv(
                    idReport,
                    "En proceso",
                    "Terminado",
                    Calendar.getInstance().toInstant().toString(),
                    latitude,
                    longitude
                )
                checkListViewModel.setAnswerPv(idAnswer, idQuestion, nameAnswer, "")
                activity?.onBackPressed()
            }

        }
        binding.btSavePictures.setOnClickListener {
            confirmationDialog.show(childFragmentManager, "dialog")
        }

        binding.btErraser.setOnClickListener {
            val report = AudioReport(
                audioSelected ?: "",
                audio64,
                ""
            )
            audioViewModel.setReadyAnswers(idQuestion, nameQuestion, idAnswer, audio64, "")
            checkListViewModel.setAnswerPv(idAnswer, idQuestion, nameAnswer, "")
            checkListViewModel.updateReportPv(idReport, "En proceso", "Borrador", "", "", "")
            audioViewModel.updateStateReport(idReport, "En proceso", "Borrador")
            audioViewModel.saveReport(report)
            activity?.onBackPressed()
        }

        binding.btTakeAudioSelect.setOnClickListener { showFilePicker() }

        audioViewModel.setAudioLimit(audioLimit)
        audioViewModel.getAudioQuestions()
        return binding.root
    }

    private fun visibilityAudioRecorded(){
        if(audioViewModel.existPreviousRecord() != "") binding.rvAudioRecord.visibility = View.VISIBLE
        else binding.rvAudioRecord.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                Log.d("data", data.data.toString())


                val uri = data.data?.lastPathSegment
                uri?.toString()?.let { it1 -> Log.d("uri", it1) }
                var nameFile: String = "ErrorFile"
                uri?.indexOf("/",0,true)?.let { it1 -> nameFile = uri.substring(it1+1, uri.length) }
                binding.lyRecordSelected.visibility = View.VISIBLE



                val newuri = data.data
                val file = File(newuri!!.path)

                binding.tvRecordSelected.text = file.name
                audioSelected = file.name
                //audioSelected = audioViewModel.convertAudioSelectedTo64Format(data.data.toString())
                //Log.d("audioSelected", audioSelected.toString())
            }

        }
    }

    private fun setUpBinding() {
        binding.viewModel = audioViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    private fun showFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        try {
            startActivityForResult(intent, REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace();
            Log.e("tag", "No activity")
        }

    }
    private fun getMiliSeoncdsOnFormat(timeMiliseconds: Int): String{
        val seconds = (timeMiliseconds/1000) %60
        val minutes = timeMiliseconds/(1000*60)
        return if(seconds < 10){
            "0$minutes:0$seconds"
        }else{
            "0$minutes:$seconds"
        }
    }

}


