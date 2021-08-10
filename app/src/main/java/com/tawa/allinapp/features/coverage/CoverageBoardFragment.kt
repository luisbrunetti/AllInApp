package com.tawa.allinapp.features.coverage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.features.coverage.composables.DateFilter
import com.tawa.allinapp.features.coverage.composables.ExpandableCard
import com.tawa.allinapp.features.coverage.composables.HeaderPage

class CoverageBoardFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                Column{
                    HeaderPage("Dashboard","Cobertura") {
                        findNavController().popBackStack()
                    }
                    Column(
                        Modifier.verticalScroll(rememberScrollState())
                    ) {
                        DateFilter()
                        ExpandableCard(
                            title = "Canal",
                            content = listOf("Oh","Moderno","Tradicional","Otros")
                        )
                        ExpandableCard(
                            title = "Tipo Retail",
                            content = listOf("Oh","Moderno","Tradicional","Otros")
                        )
                        ExpandableCard(
                            title = "Cadena",
                            content = listOf("Oh","Moderno","Tradicional","Otros")
                        )
                        ExpandableCard(
                            title = "Usuarios",
                            content = listOf("Oh","Moderno","Tradicional","Otros")
                        )
                    }
                }
            }
        }
    }
}