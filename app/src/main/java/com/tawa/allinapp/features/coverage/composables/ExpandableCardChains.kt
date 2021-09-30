package com.tawa.allinapp.features.coverage.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
fun ExpandableCardChain(
    title: String,
    content:List<String>,
    onSelected: (List<String>) -> Unit
){
    val hashCheckedItem = remember{ mutableStateMapOf<String,Boolean>()}
    content.map { hashCheckedItem[it] = false }
    val checkedList = remember { mutableListOf<String>() }
    checkedList.clear()
    var expandedState by remember { mutableStateOf(false) }
    var mainCheckState by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top),
        color = Color.White,
        elevation = 5.dp
    ) {
        Column(Modifier.fillMaxWidth()) {
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
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { expandedState = !expandedState },
                    painter = if(expandedState) painterResource(id = R.drawable.ic_less) else painterResource(id = R.drawable.ic_add),
                    contentDescription = "ArrowDropDown"
                )
            }
            if (expandedState){
                Column(Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)) {
                    val textState = remember{ mutableStateOf(TextFieldValue(""))}
                    SearchViewChain(textState)
                    LazyRow(){
                        items(checkedList as ArrayList<String>, itemContent = {item: String ->
                            NameCardChain(text = item) {
                                hashCheckedItem[item] = false
                                checkedList.remove(item)
                                onSelected(checkedList)
                                mainCheckState = false
                            }
                            Spacer(modifier = Modifier.padding(1.dp))
                        })
                    }
                    Spacer(modifier = Modifier.padding(3.dp))
                    Row{
                        Checkbox(
                            modifier = Modifier.padding(bottom = 10.dp),
                            checked = mainCheckState,
                            onCheckedChange =
                            {
                                mainCheckState = it
                                if (mainCheckState) {
                                    checkedList.clear()
                                    for (key in hashCheckedItem.keys) {
                                        checkedList.add(key)
                                        hashCheckedItem[key] = it
                                    }
                                    onSelected(content)
                                }
                                else{
                                    checkedList.clear()
                                    for (key in hashCheckedItem.keys) {
                                        hashCheckedItem[key] = it
                                    }
                                    onSelected(checkedList)
                                }
                            },
                        )
                        Text(text = " Seleccionar todos", fontSize = 16.sp)
                    }
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
                            Checkbox(
                                modifier = Modifier.padding(bottom = 10.dp),
                                //checked = if(!mainCheckState) hashCheckedItem[element]!! else mainCheckState,
                                checked = hashCheckedItem[element]!!,
                                onCheckedChange =
                                {
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
fun NameCardChain(text: String,onClick: () -> Unit){
    val colorGray = colorResource(id = R.color.labelNames)
    Card(
        modifier = Modifier
            .padding(start = 4.dp, top = 5.dp, bottom = 4.dp, end = 5.dp)
            .wrapContentWidth(Alignment.Start)
            .background(color = colorGray, shape = RoundedCornerShape(25.dp)),
        //.border(width = 1.dp, color = Color.Gray),
        elevation = 2.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.background(color = colorGray)
        ){
            Text(
                text = text,
                fontSize = 16.sp,
                modifier = Modifier
                    .wrapContentWidth(Alignment.Start)
                    //.background(Color.Transparent)
                    .padding(start = 10.dp, end = 5.dp, bottom = 6.dp, top = 5.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "close",
                modifier = Modifier
                    .padding(top= 5.dp, bottom = 3.dp, end = 3.dp,start = 3.dp)
                    //.wrapContentSize(Alignment.CenterEnd)
                    .clickable(onClick = onClick),
            )
        }
    }
}

@Composable
fun SearchViewChain(textState: MutableState<TextFieldValue>){
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
fun DefaultPreviewChain(){
    NameCard(text = "hola") {

    }
}