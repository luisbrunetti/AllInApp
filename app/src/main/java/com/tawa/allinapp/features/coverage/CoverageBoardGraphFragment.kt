package com.tawa.allinapp.features.coverage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.features.coverage.composables.HeaderPage
import com.tawa.allinapp.features.coverage.composables.StatusCard
import com.tawa.allinapp.features.coverage.composables.UserCount

class CoverageBoardGraphFragment: BaseFragment() {
    private lateinit var coverageViewModel: CoverageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        coverageViewModel = viewModel(viewModelFactory) {}
    }

    private fun initViewModels(){

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewModels()
        return ComposeView(requireContext()).apply {
            setContent {
                Column {
                    HeaderPage("Dashboard","Cobertura") {
                        findNavController().popBackStack()
                    }
                    CoverageBoardGraphPage()
                }

            }
        }
    }
    @Composable
    fun CoverageBoardGraphPage(){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 12.dp, end = 12.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Users()
                Text(modifier = Modifier.padding(12.dp), text = "Cobertura por cadena", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Status()
                Text(modifier = Modifier.padding(12.dp), text = "Status de Cumplimiento", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                StatusCard("Visitas",0.8f,100,80,10, R.color.colorIndicator1)
                StatusCard("Tareas por hacer",0.1f,100,10,80, R.color.colorIndicator2)
            }
        }
    }

    @Composable
    fun Status(){
        Row() {
            Column(
                modifier = Modifier.weight(1f).padding(10.dp)
            ) {
                Text(text = "100")
                Text(text = "100")
                Text(text = "100")
                Text(text = "100")
                Text(text = "100")
                Text(text = "100")
            }
            for (a in 0..3){
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .height(100.dp)
                            .width(10.dp),
                        shape = RoundedCornerShape(100.dp),
                        color = colorResource(id = R.color.green)
                    ) {}
                    Text(text = "Texto")
                }
            }
        }
    }

    @Composable
    fun Users(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Surface(modifier = Modifier.weight(1f)) {
                UserCount(title = "Usuarios activos",color = colorResource(id = R.color.green), qty = 3, icon = R.drawable.ic_contract2)
            }
            Surface(modifier = Modifier.weight(1f)) {
                UserCount(title = "Usuarios no reportados",color = colorResource(id = R.color.red), qty = 3,icon = R.drawable.ic_contract1)
            }
        }
    }

    @Composable
    @Preview
    fun DefaultCoverageBoardGraphPagePreview(){
        CoverageBoardGraphPage()
    }
}

