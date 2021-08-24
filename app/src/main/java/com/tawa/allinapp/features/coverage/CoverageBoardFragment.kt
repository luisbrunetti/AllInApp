package com.tawa.allinapp.features.coverage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.features.coverage.composables.DateFilter
import com.tawa.allinapp.features.coverage.composables.ExpandableCard
import com.tawa.allinapp.features.coverage.composables.HeaderPage

class CoverageBoardFragment : BaseFragment() {

    private lateinit var coverageViewModel: CoverageViewModel
    private var selectedChannel:String? = ""
    private var selectedRetail:String? = ""
    private var selectedChain:String? = ""
    private var selectedUser:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        coverageViewModel = viewModel(viewModelFactory) {}
    }

    private fun initViewModels(){
        coverageViewModel.getChannels()
        coverageViewModel.getRetails()
        coverageViewModel.getUserList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModels()
        return ComposeView(requireContext()).apply {
            setContent {
                Column{
                    HeaderPage("Dashboard","Cobertura") {
                        findNavController().popBackStack()
                    }
                    Filters()
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorLayoutTop)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(12.dp),
                        onClick = { findNavController().navigate(CoverageBoardFragmentDirections.actionCoverageBoardFragmentToCoverageBoardGraphFragment()) }
                    ) {
                        Text("Buscar", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }

    @Composable
    fun Filters(){
        var startDate by remember { mutableStateOf<String>("") }
        var endDate by remember { mutableStateOf<String>("") }

        val channels by coverageViewModel.channels.observeAsState()
        val retails by coverageViewModel.retails.observeAsState()
        val chains by coverageViewModel.chains.observeAsState()
        val userList by coverageViewModel.userList.observeAsState()
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            DateFilter(startDate,endDate,{ startDate = it },{ endDate = it })
            channels?.let { c ->
                ExpandableCard(
                    title = "Canal",
                    content = c.map { it.description?:"" }
                ){ list ->
                    if(list.isNotEmpty())
                        selectedChannel = c.find { it.description == list.first() }?.id
                    coverageViewModel.getChains(selectedChannel?:"",selectedRetail?:"")
                }
            }
            retails?.let { c ->
                ExpandableCard(
                    title = "Tipo Retail",
                    content = c.map { it.description?:"" }
                ){ list ->
                    if(list.isNotEmpty())
                        selectedRetail = c.find { it.description == list.first() }?.id
                    coverageViewModel.getChains(selectedChannel?:"",selectedRetail?:"")
                }
            }
            chains?.let { c ->
                ExpandableCard(
                    title = "Cadena",
                    content = c.map { it.description?:"" }
                ){ list ->
                    selectedChain = c.find { it.description == list.first() }?.id
                }
            }
            userList?.let { c ->
                ExpandableCard(
                    title = "Usuarios",
                    content = c.map { it.fullName?:"" }
                ){ list ->
                    selectedUser = c.find { it.fullName == list.first() }?.id
                }
            }
        }
    }
}