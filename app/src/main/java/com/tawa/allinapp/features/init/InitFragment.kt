package com.tawa.allinapp.features.init

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.CheckinSelectorDialogFragment
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.dialog.SelectorDialogFragment
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInitBinding
import java.time.LocalDateTime


class InitFragment : BaseFragment() {

    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentInitBinding
    var flag= true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInitBinding.inflate(inflater)
        showSelector("")
        initViewModel = viewModel(viewModelFactory) {

            observe(dayState, {
                it?.let {
                    if(it)
                    {
                        val currentDay = getString(R.string.current_day, getDayWeek(),getDayMonth(),getMonth(),getYear())
                        binding.currentDay.text  = currentDay
                    }
                }
            })

           /* observe(startCheckIn, {
                it?.let {
                    if(it)
                    {
                       Toast.makeText(context,"CLICK CHECK",Toast.LENGTH_SHORT).show()
                    }

                }
            })*/


        }

        binding.button3.setOnClickListener(View.OnClickListener {

        //Toast.makeText(context,""+current.,Toast.LENGTH_SHORT).show()
            showSelectorCheckIn("")
        })


        return binding.root
    }

        private fun showSelector(message:String?){
        val dialog = SelectorDialogFragment(this)
        dialog.show(childFragmentManager, "dialog")
    }

        private fun showSelectorCheckIn(message:String?){
        val dialog = CheckinSelectorDialogFragment(this)
        dialog.show(childFragmentManager, "dialog")
    }



    /*private fun onCreateDialog(): Dialog {
       return activity?.let {
           val builder = AlertDialog.Builder(it)
           val inflater = requireActivity().layoutInflater

           builder.setView(inflater.inflate(R.layout.dialog_home,null))

           builder.create()
       } ?: throw IllegalStateException("Activity cannot be null")
   }*/






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



