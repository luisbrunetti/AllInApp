package com.tawa.allinapp.features.coverage.composables

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tawa.allinapp.R
import java.util.*

@Composable
fun ExpandableCard(
    title: String,
    content:List<String>,
    onSelected: (List<String>) -> Unit
){
    var expandedState by remember { mutableStateOf(true) }
    val hashCheckedItem = remember{ mutableStateMapOf<String,Boolean>()}
    var mainCheckState by remember { mutableStateOf(false) }
    val checkedList = remember { mutableListOf<String>() }
    content.map { hashCheckedItem[it] = false }
    Log.d("listChckedeItem", hashCheckedItem.values.toString())
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable { expandedState = !expandedState },
        color = Color.White,
        elevation = 5.dp
    ) {
        Column(
            Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = title,
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
                Icon(
                    modifier = Modifier.padding(12.dp),
                    painter = if(expandedState) painterResource(id = R.drawable.ic_less) else painterResource(id = R.drawable.ic_add),
                    contentDescription = "ArrowDropDown"
                )
            }
            if (expandedState){
                Column(Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)) {
                    val textState = remember{ mutableStateOf(TextFieldValue(""))}
                    SearchView(textState)
                    Row{
                        Checkbox(
                            modifier = Modifier.padding(bottom = 10.dp),
                            checked = mainCheckState,
                            onCheckedChange =
                            {
                                mainCheckState = it
                                if (mainCheckState) {
                                    for (key in hashCheckedItem.keys) { hashCheckedItem[key] = it }
                                    onSelected(content)
                                }
                                else{
                                    for (key in hashCheckedItem.keys) { hashCheckedItem[key] = it }
                                    onSelected(checkedList)
                                }
                            },
                        )
                        Text(text = " Seleccionar todos", fontSize = 16.sp)
                    }
                    Log.d("asd",textState.value.text)
                    var listFiltered = content
                    if(textState.value.text != ""){
                        listFiltered = listFiltered.filter {
                            it.lowercase(Locale.ROOT).contains(textState.value.text.lowercase(Locale.ROOT))
                        }
                    }
                    for (element in listFiltered){
                        Row(
                            Modifier.padding(start = 20.dp)
                        ){
                            var checkState by remember { mutableStateOf(false) }
                            //Log.d("listChecked", listCheckedItem[0].toString())
                            Checkbox(
                                modifier = Modifier.padding(bottom = 10.dp),
                                //checked = if(!mainCheckState) hashCheckedItem[element]!! else mainCheckState,
                                checked = hashCheckedItem[element]!!,
                                onCheckedChange =
                                {
                                    checkState = it
                                    if(mainCheckState && it){
                                        onSelected(content)
                                    } else {
                                        if(it){
                                            checkedList.add(element)
                                            hashCheckedItem[element] = it
                                        }
                                        else {
                                            mainCheckState=false
                                            checkedList.remove(element)
                                            hashCheckedItem[element] = it
                                        }
                                        onSelected(checkedList)
                                    }
                                },
                                colors = CheckboxDefaults.colors(colorResource(id = R.color.colorPrimary) )
                            )
                            Text(text = " $element", fontSize = 18.sp)
                        }
                    }
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Gray
            ) {}
        }
    }
}

@Composable
fun SearchView(textState: MutableState<TextFieldValue>){
    TextField(
        value = textState.value,
        onValueChange = { value ->
            textState.value = value
        },
        modifier = Modifier
            .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
            .height(50.dp)
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        trailingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp),
                tint = Color.Black
            )
        },
        singleLine = true ,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Transparent,
            cursorColor = Color.Black,
            trailingIconColor = Color.Black,
            backgroundColor = colorResource(id = R.color.white),
            focusedIndicatorColor = Color.Transparent,
            //unfocusedIndicatorColor = Color.White,
            disabledIndicatorColor = Color.White
        )
    )
    Spacer(modifier = Modifier.padding(6.dp))
}
@Composable
@Preview
fun DefaultPreview(){
    ExpandableCard(
        title = "Canal",
        content = listOf("Lorem","ipsum","dolor","sit amet")
    ){}
    //SearchView(content = listOf("Lorem","ipsum","dolor","sit amet"))
}