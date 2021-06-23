package com.tawa.allinapp.features.init.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInitBinding
import com.tawa.allinapp.features.init.InitViewModel


class InitFragment : BaseFragment() {

    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentInitBinding

    private var checkOutDialog: CheckOutDialogFragment? = null
    private var checkIn:Boolean = true
    var _user = ""
    private lateinit var _place: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInitBinding.inflate(inflater)
        showSelector()
        initViewModel = viewModel(viewModelFactory) {
            observe(dayState, { it?.let { if(it) {
                val currentDay = getString(R.string.current_day, getDayWeek(),getDayMonth(),getMonth(),getYear())
                binding.currentDay.text  = currentDay
            } }})
            observe(checkInMode, { it?.let {
                checkIn = it
                hideProgressDialog()
            }})
            observe(idUser, { it?.let {
                _user = it
            }})
            observe(successCheckOut, { it?.let {
                initViewModel.getCheckMode()
            } })
            failure(failure, ::handleFailure)
        }
        initViewModel.getIdUser()
        binding.btCheckIn.setOnClickListener{
            if(checkIn) showSelectorCheckIn()
            else showCheckOut()
        }
        return binding.root
    }

    private fun showSelector(){
        val dialog = SelectorDialogFragment(this)
        dialog.show(childFragmentManager, "dialog")
    }

    private fun showSelectorCheckIn(){
        val dialog = CheckInDialogFragment(this)
        dialog.listener = object : CheckInDialogFragment.Callback {
            override fun onAccept(place:String) {
                _place = place
                initViewModel.getCheckMode()
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    private fun showCheckOut(){
        checkOutDialog = CheckOutDialogFragment.newInstance(_place, _user)
        checkOutDialog?.listener = object : CheckOutDialogFragment.Callback {
            override fun onAccept() {
                initViewModel.setCheckOut(_user,_place)
                //initViewModel.getCheckMode()
            }
        }
        checkOutDialog?.show(childFragmentManager, "checkOutDialog")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
    }

    private fun setUpBinding() {
        binding.viewModel = initViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}



