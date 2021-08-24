package com.tawa.allinapp.features.coverage.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tawa.allinapp.R

@Composable
fun UserCount(
    title: String,
    qty:Int,
    @DrawableRes icon: Int,
    color: Color = Color.Unspecified,
){
    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(8.dp),
        color = color,
    ) {
        Column (
            modifier = Modifier.padding(top = 12.dp, start = 12.dp).fillMaxWidth()
        ){
            Text(modifier = Modifier.fillMaxWidth().weight(1f), text = title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Normal)
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                Text(modifier = Modifier.weight(1f), text = "$qty", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Image(
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    painter = painterResource(id = icon),
                    contentDescription = "IconUserCountDown"
                )
            }
        }
    }
}

@Composable
@Preview
fun DefaultUserTypePreview(){
    UserCount(title = "Usuarios activos",color = Color.Black, qty = 3, icon = R.drawable.ic_contract1)
}