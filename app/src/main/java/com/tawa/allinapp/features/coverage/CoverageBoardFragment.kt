package com.tawa.allinapp.features.coverage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentCalendarBinding
import com.tawa.allinapp.features.coverage.composables.DateFilter
import com.tawa.allinapp.features.coverage.composables.ExpandableCard
import com.tawa.allinapp.features.coverage.composables.HeaderPage
import com.tawa.allinapp.models.Channel

class CoverageBoardFragment : BaseFragment() {

    private lateinit var coverageViewModel: CoverageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        coverageViewModel = viewModel(viewModelFactory) {}
    }

    private fun initViewModels(){
        coverageViewModel.getChannels()
        coverageViewModel.getRetails()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModels()
        return ComposeView(requireContext()).apply {
            setContent {
                var startDate by remember { mutableStateOf<String>("") }
                var endDate by remember { mutableStateOf<String>("") }

                val channels by coverageViewModel.channels.observeAsState()
                val retails by coverageViewModel.retails.observeAsState()

                Column{
                    HeaderPage("Dashboard","Cobertura") {
                        findNavController().popBackStack()
                    }
                    Column(
                        Modifier.verticalScroll(rememberScrollState())
                    ) {
                        DateFilter(startDate,endDate,{ startDate = it },{ endDate = it })
                        channels?.map { it.description?:"" }?.let {
                            ExpandableCard(
                                title = "Canal",
                                content = it
                            ){ list -> val a = list}
                        }
                        retails?.map { it.description?:"" }?.let {
                            ExpandableCard(
                                title = "Tipo Retail",
                                content = it
                            ){ list -> val a = list}
                        }
                        ExpandableCard(
                            title = "Cadena",
                            content = listOf("Oh","Moderno","Tradicional","Otros")
                        ){ val a = it}
                        ExpandableCard(
                            title = "Usuarios",
                            content = listOf("Oh","Moderno","Tradicional","Otros")
                        ){ val a = it}
                    }
                }
            }
        }
    }
}