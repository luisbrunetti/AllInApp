package com.tawa.allinapp.features.coverage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.features.coverage.composables.HeaderPage
import com.tawa.allinapp.features.coverage.composables.StatusCard
import com.tawa.allinapp.features.coverage.composables.UserCount
import com.tawa.allinapp.models.CoverageGraph

class CoverageBoardGraphFragment: BaseFragment() {
    private lateinit var coverageViewModel: CoverageViewModel
    private var graph:CoverageGraph? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        coverageViewModel = viewModel(viewModelFactory) {

        }
    }

    private fun initArgs(){
        graph = arguments?.getParcelable("graph")
        Log.d("graph",graph.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initArgs()
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
            //Log.d("data", )
            val visits = if (graph?.visits?.total==0.0) 0.0f
            else (graph?.visits?.concluded?.div(graph?.visits?.total?:0.0))?.toFloat()?:0.0f
            val reports =
                if (graph?.reports?.total==0.0) 0.0f
                else (graph?.reports?.concluded?.div(graph?.reports?.total?:0.0))?.toFloat()?:0.0f
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 12.dp, end = 12.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Users()

                Text(modifier = Modifier.padding(12.dp), text = "Cobertura por cadena",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(10.dp))
                    Surface(
                        Modifier
                            .height(15.dp)
                            .width(15.dp), shape = RoundedCornerShape(100.dp), color = colorResource(id = R.color.green)) {}
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Mayor 75%", fontSize = 14.sp)
                    Spacer(Modifier.width(10.dp))
                    Surface(
                        Modifier
                            .height(15.dp)
                            .width(15.dp), shape = RoundedCornerShape(100.dp), color = colorResource(id = R.color.red)) {}
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Menor 75%", fontSize = 14.sp)
                }

                Spacer(Modifier.height(10.dp))

                Status()

                Text(modifier = Modifier.padding(12.dp), text = "Status de Cumplimiento", fontWeight = FontWeight.Bold, fontSize = 24.sp)

                StatusCard("Visitas",visits,graph?.visits?.total?.toInt()?:0,graph?.visits?.concluded?.toInt()?:0,graph?.visits?.pending?.toInt()?:0, R.color.colorIndicator1)

                StatusCard("Tareas por hacer",reports,graph?.reports?.total?.toInt()?:0,graph?.reports?.concluded?.toInt()?:0,graph?.reports?.pending?.toInt()?:0, R.color.colorIndicator2)
            }
        }
    }


    @Composable
    fun DrawBar(size:Float){
        var animationPlayed by remember {
            mutableStateOf(false)
        }
        val percentage = animateFloatAsState(
            targetValue = if (animationPlayed) size else 0f,
            animationSpec = tween(
                durationMillis = 2000,
                delayMillis = 0
            )
        )
        LaunchedEffect(key1 = true){
            animationPlayed = true
        }
        val red = colorResource(id = R.color.red)
        val yellow = colorResource(id = R.color.yellow)
        val green = colorResource(id = R.color.green)
        Canvas(modifier = Modifier.fillMaxSize()){
            drawLine(
                start = Offset(x = 0f, y = -0f),
                end = Offset(x = 0f,y = -percentage.value),
                color = when {
                    percentage.value <= (540 * 50.0)/100 -> red
                    (540 * 50.0)/100 < percentage.value  && percentage.value < (540 * 75.0)/100 -> yellow
                    percentage.value >= (540 * 75.0)/100 -> green
                    else -> green
                },
                strokeWidth = 15f,
                cap = StrokeCap.Round,
            )
        }
    }

    @Composable
    fun Status(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.padding(bottom = 20.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "100", modifier = Modifier.padding(bottom = 19.dp))
                Text(text = "80", modifier = Modifier.padding(bottom = 19.dp))
                Text(text = "60", modifier = Modifier.padding(bottom = 19.dp))
                Text(text = "40", modifier = Modifier.padding(bottom = 19.dp))
                Text(text = "20", modifier = Modifier.padding(bottom = 19.dp))
                Text(text = "0" , modifier = Modifier.padding(bottom = 19.dp))
            }
            val cover = graph?.coverage?.infoChain
            for (a in 0 until (cover?.size ?: 1)){
                Log.d("task", " Tareas por realizar ${cover?.get(a)?.tasksToDo.toString()} \nTareas terminadas -> ${cover?.get(a)?.tasksFinished.toString()}" )
                val size = if(cover?.get(a)?.tasksFinished == 0.0) 0.0
                else (cover?.get(a)?.tasksToDo?.plus(cover[a].tasksFinished)?.div(cover[a].tasksFinished))?:0.0
                Log.d("sizeBar",size.toString())
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DrawBar(size = ((size*540)/100).toFloat())
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("${cover?.get(a)?.chainName}",
                        modifier = Modifier.height(20.dp),
                        fontSize = 10.sp,
                        style = TextStyle(textAlign = TextAlign.Center,),
                    )
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
                UserCount(title = "Usuarios activos",color = colorResource(id = R.color.green), qty = graph?.user?.active?.toInt()?:0, icon = R.drawable.ic_contract2)
            }
            Surface(modifier = Modifier.weight(1f)) {
                UserCount(title = "Usuarios no reportados",color = colorResource(id = R.color.red),
                    qty = (graph?.user?.active?.toInt() ?: 0) - (graph?.user?.activeReported?.toInt() ?:0),icon = R.drawable.ic_contract1)
            }
        }
    }

    @Composable
    @Preview
    fun DefaultCoverageBoardGraphPagePreview(){
        CoverageBoardGraphPage()
    }
}

