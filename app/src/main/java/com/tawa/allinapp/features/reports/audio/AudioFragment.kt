package com.tawa.allinapp.features.reports.audio

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.*
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentAudioBinding
import com.tawa.allinapp.features.reports.standard.CheckListViewModel


class AudioFragment : BaseFragment() {

    private lateinit var binding: FragmentAudioBinding
    private lateinit var audioViewModel: AudioViewModel
    private lateinit var checkListViewModel: CheckListViewModel
    private  var idQuestion = ""
    private  var nameQuestion = ""
    private  var idAnswer =""
    private  var nameAnswer = ""
    private  var audio64 = ""
    private var idReport = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        checkPermissions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAudioBinding.inflate(inflater)
        arguments?.getString("id").toString().also {idRep->
            idReport=idRep
        }

        checkListViewModel = viewModel(viewModelFactory){

        }

        audioViewModel = viewModel(viewModelFactory){
            observe(fileString,{ it?.let {
                if (it.isNotEmpty()){
                    audio64 = it
                    binding.rvAudioRecord.visible()
                }else
                    binding.rvAudioRecord.invisible()
            }})
            observe(recording,{ it?.let {
                if (it){
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.chronometer.start()
                    binding.rvAudioRecord.invisible()
                }
                else{
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.chronometer.stop()
                    binding.rvAudioRecord.visible()
                }
            }})
            observe(successAudio,{
                it?.let {
                    for(audio in it)
                    {
                        idQuestion = audio.id
                        nameQuestion = audio.questionName
                        audioViewModel.getAnswersAudio(audio.id)
                    }
                }
            })
            observe(answersAudio,{
                it?.let {
                    for(ans in it)
                    {
                        idAnswer = ans.id
                        nameAnswer = ans.answerName
                    }
                }
            })
            failure(failure, { it?.let {
                hideProgressDialog()
                when(it){
                    is Failure.NetworkConnection    -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    is Failure.ServerError          -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    else                            -> MessageDialogFragment.newInstance(getString(R.string.error_unknown)).show(childFragmentManager, "dialog")
                }
            }})
        }
        binding.ivClose.setOnClickListener { binding.rvAudioRecord.invisible() }
        binding.btSavePictures.setOnClickListener {
            audioViewModel.setReadyAnswers(idQuestion,nameQuestion,idAnswer,audio64,"")
            checkListViewModel.updateReportPv(idReport,"En proceso","Terminado")
            checkListViewModel.setAnswerPv(idAnswer,idQuestion,nameAnswer,"")
            audioViewModel.updateStateReport(idReport, "En proceso","Terminado")
            activity?.onBackPressed()
        }

        binding.btErraser.setOnClickListener {
            audioViewModel.setReadyAnswers(idQuestion,nameQuestion,idAnswer,audio64,"")
            checkListViewModel.setAnswerPv(idAnswer,idQuestion,nameAnswer,"")
            checkListViewModel.updateReportPv(idReport,"En proceso","Borrador")
            audioViewModel.updateStateReport(idReport, "En proceso","Borrador")
            activity?.onBackPressed()
        }

        audioViewModel.getAudioQuestions()
        return binding.root
    }

    private fun setUpBinding() {
        binding.viewModel = audioViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}