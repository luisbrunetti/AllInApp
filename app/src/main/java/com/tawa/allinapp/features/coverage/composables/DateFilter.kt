package com.tawa.allinapp.features.coverage.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DateFilter(
    startDate:String,
    endDate:String,
    onStart: (start:String) ->Unit,
    onEnd: (end:String) ->Unit
){
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(text = "Fecha",fontSize = 16.sp,)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    value = startDate,
                    onValueChange = { onStart(it) }
                )
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    value = endDate,
                    onValueChange = { onEnd(it) }
                )
            }
        }
    }
}
