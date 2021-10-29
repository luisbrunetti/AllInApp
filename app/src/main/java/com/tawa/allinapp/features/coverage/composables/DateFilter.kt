package com.tawa.allinapp.features.coverage.composables

import android.widget.Space
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.TranslateObject
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.format.DateTimeFormatter

@Composable
fun DateFilter(
    translateObject: TranslateObject,
    onStart: (start:String) ->Unit,
    onEnd: (end:String) ->Unit,
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(text = translateObject.findTranslate("tvTimeDateFilter").toString(),fontSize = 16.sp,)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(Modifier.weight(1f)) {
                    ReadonlyDateField(translateObject.findTranslate("tvStartDateFilter").toString()){ onStart(it) }
                }
                Surface(Modifier.weight(1f)) {
                    ReadonlyDateField(translateObject.findTranslate("tvFinalDateFilter").toString(), true){ onEnd(it) }
                }
            }
        }
    }
}

@Composable
fun ReadonlyDateField(
    label:String,
    end:Boolean=false,
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
    Box {
        val shape = if(!end) RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp) else RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
        if (!end)
            TextField(
                shape = shape,
                modifier = Modifier.border(1.dp, Color.Gray,shape = shape),
                value = startState,
                onValueChange = { startState = it},
                label = { Text(text = label, color = Color.Gray) },
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_calendar), tint = colorResource(id = R.color.blue), contentDescription = "CalendarIcon") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable(onClick = { dialog.show() }),
            )
        if(end)
            TextField(
                shape = shape,
                modifier = Modifier.border(1.dp, Color.Gray,shape = shape),
                value = startState,
                onValueChange = { startState = it},
                label = { Text(text = label, color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = { dialog.show() }),
        )
    }
}

@Composable
@Preview
fun DefaultReadonlyDateFieldPreview(){
    ReadonlyDateField("Dia"){}
}