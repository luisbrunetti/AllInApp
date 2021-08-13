package com.tawa.allinapp.features.coverage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentCalendarBinding
import com.tawa.allinapp.features.coverage.composables.DateFilter
import com.tawa.allinapp.features.coverage.composables.ExpandableCard
import com.tawa.allinapp.features.coverage.composables.HeaderPage

class CoverageBoardFragment : BaseFragment() {

    private lateinit var coverageViewModel: CoverageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    private fun initViewModels(){
        coverageViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {

                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModels()
        return ComposeView(requireContext()).apply {
            setContent {
                var startDate by remember { mutableStateOf<String>("") }
                var endDate by remember { mutableStateOf<String>("") }
                Column{
                    HeaderPage("Dashboard","Cobertura") {
                        findNavController().popBackStack()
                    }
                    Column(
                        Modifier.verticalScroll(rememberScrollState())
                    ) {
                        DateFilter(startDate,endDate,{ startDate = it },{ endDate = it })
                        ExpandableCard(
                            title = "Canal",
                            content = listOf("Oh","Moderno","Tradicional","Otros")
                        ){ val a = it}
                        ExpandableCard(
                            title = "Tipo Retail",
                            content = listOf("Oh","Moderno","Tradicional","Otros")
                        ){ val a = it}
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