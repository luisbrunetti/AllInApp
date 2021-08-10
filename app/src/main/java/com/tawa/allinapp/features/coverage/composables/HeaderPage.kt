package com.tawa.allinapp.features.coverage.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tawa.allinapp.R
import androidx.navigation.fragment.findNavController

@Composable
fun HeaderPage(
    title: String,
    subTitle: String,
    onClick: () -> Unit
){
    Surface(
        shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
    ) {
        Box() {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                colorResource(id = R.color.colorLayoutTop),
                                colorResource(id = R.color.colorLayoutTopGradient),
                            )
                        )
                    )
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_reports),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                    Text(
                        text = title,
                        color = colorResource(id = R.color.white),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.padding(start = 48.dp))
                    Text(
                        text = subTitle,
                        color = colorResource(id = R.color.white),
                        fontSize = 14.sp,
                    )
                }
            }
            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp),
                painter = painterResource(id = R.drawable.computer),
                contentDescription = "IconHeader",
            )
        }
    }

}

@Composable
@Preview
fun DefaultPreviewHeaderPage(){
    HeaderPage("Dashboard","Cobertura") { }
}