package com.tawa.allinapp.features.coverage.composables

import androidx.annotation.ColorRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@Composable
fun StatusCard(
    title:String,
    percent:Float,
    total:Int,
    finished:Int,
    pending:Int,
    @ColorRes background: Int
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp),
        shape = RoundedCornerShape(10.dp),
        color = colorResource(id = background),
        elevation = 5.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                color = colorResource(id = background)
            ) {
                CircularProgress(percent)
            }
            Surface(
                modifier = Modifier.weight(2f),
                color = colorResource(id = background)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ){
                    Text(modifier = Modifier.padding(bottom = 8.dp), text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Total: $total", fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(Modifier.height(8.dp).width(20.dp).padding(end = 5.dp), shape = RoundedCornerShape(10.dp), color = colorResource(id = R.color.green)) {}
                        Text(text = "Concluidas: $finished", fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(Modifier.height(8.dp).width(20.dp).padding(end = 5.dp), shape = RoundedCornerShape(10.dp), color = colorResource(id = R.color.red)) {}
                        Text(text = "Pendientes: $pending", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CircularProgress(percent:Float){
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val percentage = animateFloatAsState(
        targetValue = if (animationPlayed) percent else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            delayMillis = 0
        )
    )
    LaunchedEffect(key1 = true){
        animationPlayed = true
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        val red = colorResource(id = R.color.red)
        val yellow = colorResource(id = R.color.yellow)
        val green = colorResource(id = R.color.green)
        Canvas(modifier = Modifier.fillMaxSize()){
            drawArc(
                color = Color.White,
                -90f,
                360f,
                useCenter = false,
                style = Stroke(4.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = when {
                    percentage.value <= 0.5 -> red
                    0.5 < percentage.value  && percentage.value < 0.75 -> yellow
                    percentage.value >= 0.75 -> green
                    else -> green
                },
                -90f,
                360 * percentage.value,
                useCenter = false,
                style = Stroke(4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(text = "${(percentage.value * 100).roundToInt()}%", fontSize = 18.sp)
    }
}

@Composable
@Preview
fun DefaultStatusCardPreview(){
    StatusCard("Visitas",0.8f,100,80,10, R.color.colorIndicator1)
}