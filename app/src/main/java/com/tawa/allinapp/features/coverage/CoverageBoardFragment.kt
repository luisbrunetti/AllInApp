package com.tawa.allinapp.features.coverage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.features.coverage.composables.DateFilter
import com.tawa.allinapp.features.coverage.composables.ExpandableCard
import com.tawa.allinapp.features.coverage.composables.HeaderPage

class CoverageBoardFragment : BaseFragment() {

    private lateinit var coverageViewModel: CoverageViewModel
    private var selectedChannel:List<String>? = null
    private var selectedRetail:List<String>? = null
    private var selectedChain:List<String>? = null
    private var selectedUser:List<String>? = null
    private var startDate:String? = null
    private var endDate:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        coverageViewModel = viewModel(viewModelFactory) {
            observe(graph, { it?.let {
                findNavController().navigate(CoverageBoardFragmentDirections.actionCoverageBoardFragmentToCoverageBoardGraphFragment(it))
            }})
        }
    }

    private fun initViewModels(){
        coverageViewModel.getChannels()
        coverageViewModel.getRetails()
        coverageViewModel.getUserList()
        coverageViewModel.getChains(emptyList(),emptyList())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initViewModels()
        return ComposeView(requireContext()).apply {
            setContent {
                Column(
                    Modifier.verticalScroll(rememberScrollState())
                ){
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
                        onClick = {
                            coverageViewModel.getGraph(startDate,endDate,selectedUser,selectedChain)
                        }
                    ) {
                        Text("Buscar", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }

    @Composable
    fun Filters(){

        val channels by coverageViewModel.channels.observeAsState()
        val retails by coverageViewModel.retails.observeAsState()
        val chains by coverageViewModel.chains.observeAsState()
        val userList by coverageViewModel.userList.observeAsState()
        Column(

        ) {
            DateFilter(
                { startDate = it },{ endDate = it }
            )
            channels?.let { ch ->
                ExpandableCard(
                    title = "Canal",
                    content = ch.map { it.description?:"" }
                ){ list ->
                    if(list.isNotEmpty()){
                        selectedChannel = ch.filter { c ->
                            c.description == list.find { it == c.description }
                        }.map { it.id?:"" }
                        coverageViewModel.getChains(selectedChannel?: emptyList(),selectedRetail?: emptyList())
                    }
                    else
                        coverageViewModel.getChains(emptyList(),selectedRetail?: emptyList())
                }
            }
            retails?.let { r ->
                ExpandableCard(
                    title = "Tipo Retail",
                    content = r.map { it.description?:"" }
                ){ list ->
                    if(list.isNotEmpty()){
                        selectedRetail = r.filter { c ->
                            c.description == list.find { it == c.description }
                        }.map { it.id?:"" }
                        coverageViewModel.getChains(selectedChannel?: emptyList(),selectedRetail?: emptyList())
                    }
                    else
                        coverageViewModel.getChains(selectedChannel?: emptyList(),emptyList())
                }
            }
            chains?.let { c ->
                ExpandableCard(
                    title = "Cadena",
                    content = c.map { it.description?:"" }
                ){ list ->
                    selectedChain = c.map{ it.description }.flatMap { e -> list.filter { e == it }}
                }
            }
            userList?.let { u ->
                ExpandableCard(
                    title = "Usuarios",
                    content = u.map { it.fullName?:"" }
                ){ list ->
                    if(list.isNotEmpty())
                        selectedUser = u.filter { c ->
                            c.fullName == list.find { it == c.fullName }
                        }.map { it.id?:"" }
                }
            }
        }
    }
}