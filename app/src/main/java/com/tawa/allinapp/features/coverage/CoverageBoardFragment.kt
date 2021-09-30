package com.tawa.allinapp.features.coverage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.features.coverage.composables.DateFilter
import com.tawa.allinapp.features.coverage.composables.ExpandableCard
import com.tawa.allinapp.features.coverage.composables.ExpandableCardChain
import com.tawa.allinapp.features.coverage.composables.HeaderPage
import com.tawa.allinapp.models.Chain

class CoverageBoardFragment : BaseFragment() {

    private lateinit var coverageViewModel: CoverageViewModel
    private var selectedChannel:List<String>? = null
    private var selectedChain:List<String>? = null
    private var selectedRetail:List<String>? = null
    private var selectedUser:List<String>? = null
    private var mutableListChain = mutableStateListOf<String>()
    private var listAllChains = ArrayList<Chain>()


    private var startDate:String? = null
    private var endDate:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    private fun initViewModels(){
        coverageViewModel.getChannels()
        coverageViewModel.getRetails()
        coverageViewModel.getUserList()
        coverageViewModel.getAllChains(emptyList(),emptyList())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        coverageViewModel = viewModel(viewModelFactory) {
            observe(graph, { it?.let {
                findNavController().navigate(CoverageBoardFragmentDirections.actionCoverageBoardFragmentToCoverageBoardGraphFragment(it))
            }})
            observe(failure,{
                it?.let {
                    Log.d("failure",failure.value.toString())
                }
            })
            observe(chains,{
                it?.let {
                    mutableListChain.clear()
                    for(element in it){ mutableListChain.add(element.description!!) }
                }
            })
            observe(allChains,{
                it?.let {
                    listAllChains.clear()
                    mutableListChain.clear()
                    listAllChains.addAll(it)
                    mutableListChain.addAll(it.map { chain -> chain.description!! })
                }
            })

        }
        initViewModels()
        return ComposeView(requireContext()).apply {
            setContent {
                main()
            }
        }
    }

    @Composable
    fun Filters(){
        val channels by coverageViewModel.channels.observeAsState()
        //val chains by coverageViewModel.chains.observeAsState()//
        val retails by coverageViewModel.retails.observeAsState()//
        val userList by coverageViewModel.userList.observeAsState()

        Column{
            DateFilter({ startDate = it },{ endDate = it })

            channels?.let { ch ->
                Log.d("Channels -> ",channels.toString())
                ExpandableCard(
                    title = "Canal",
                    listElementsPrincipal = ch.map { it.description?:"" }
                ){ list ->
                    if(list.isNotEmpty()){
                        selectedChannel = ch.filter { c ->
                            c.description == list.find {
                                it == c.description
                            }
                        }.map { it.id?:""}
                        coverageViewModel.getChains(selectedChannel?: emptyList(),selectedRetail?: emptyList())
                    }
                    else coverageViewModel.getChains(emptyList(),selectedRetail?: emptyList())
                }
            }
            retails?.let { r ->
                ExpandableCard(
                    title = "Tipo Retail",
                    listElementsPrincipal = r.map {
                        it.description?:""
                    }
                ){ list ->
                    //Log.d("retails",list[0].toString())
                    if(list.isNotEmpty()){
                        selectedRetail = r.filter { c ->
                            c.description == list.find {
                                it == c.description
                            }
                        }.map { it.id?:"" }
                        coverageViewModel.getChains(selectedChannel?: emptyList(),selectedRetail?: emptyList())
                    }
                    else coverageViewModel.getChains(selectedChannel?: emptyList(),emptyList())
                }
            }
            ExpandableCard(
                title = "Cadena",
                listElementsPrincipal = mutableListChain
            ) { list ->
                //Log.d("chainsSelected", list.toString())
                selectedChain = listAllChains.filter { chain ->
                    chain.description == list.find { desc -> desc == chain.description }
                }.map {
                    it.id ?: ""
                }
                Log.d("selectedChain", selectedChain.toString())
            }

            userList?.let { u ->
                //Log.d("userList",u[0].toString())
                ExpandableCard(
                    title = "Usuarios",
                    listElementsPrincipal = u.map { it.fullName?:"" }
                ){ list ->
                    if(list.isNotEmpty())
                        selectedUser = u.filter { c ->
                            c.fullName == list.find { it == c.fullName }
                        }.map { it.id?:"" }
                }
            }
        }
    }

    @Composable
    fun main(){
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ){
            HeaderPage("Dashboard","Cobertura") {
                findNavController().popBackStack()
            }
            Filters()
            Button(
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = colorResource(id = R.color.colorLayoutTop)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(12.dp),
                onClick = {
                    Log.d("CoverageBoard",
                        "startdate -> $startDate" +
                                " \nendData -> $endDate" +
                                " \nselectedUser -> $selectedUser" +
                                " \nselectedChain -> $selectedChain")
                    if(startDate == null || endDate == null) notify(requireActivity(), R.string.select_dates)
                    else coverageViewModel.getGraph(startDate,endDate,selectedUser,selectedChain)
                }
            ) {
                Text("Buscar", color = Color.White, fontSize = 18.sp)
            }
        }
    }

    @Preview("Text Preview")
    @Composable
    fun MainContainer(){
        main()
    }
}