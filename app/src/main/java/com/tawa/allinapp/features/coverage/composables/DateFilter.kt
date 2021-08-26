package com.tawa.allinapp.features.coverage.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tawa.allinapp.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.format.DateTimeFormatter

@Composable
fun DateFilter(
    onStart: (start:String) ->Unit,
    onEnd: (end:String) ->Unit,
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
                Surface(
                    modifier = Modifier.weight(1f)
                ) {
                    ReadonlyDateField("Inicio"){ onStart(it) }
                }
                Surface(
                    modifier = Modifier.weight(1f)
                ) {
                    ReadonlyDateField("Final"){ onEnd(it) }
                }
            }
        }
    }
}

@Composable
fun ReadonlyDateField(
    label:String,
    onDate:(date:String) -> Unit,
) {
    var startState by remember { mutableStateOf(TextFieldValue()) }
    val dialog = MaterialDialog()
    dialog.build(buttons = {
        positiveButton("Ok")
        negativeButton("Cancel")
    }) {
        datepicker { date ->
            val formattedDate = date.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            startState = TextFieldValue(formattedDate)
            onDate(formattedDate)
        }
    }
    Box (
        modifier = Modifier.padding(5.dp),
    ){
        TextField(
            value = startState,
            shape = RoundedCornerShape(8.dp),
            onValueChange = { startState = it},
            label = { Text(text = label) },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_calendar)  , contentDescription = "CalendarIcon")},
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White,),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = { dialog.show() }),
        )
    }
}