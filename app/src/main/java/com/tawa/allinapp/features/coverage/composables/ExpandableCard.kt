package com.tawa.allinapp.features.coverage.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tawa.allinapp.R

@Composable
fun ExpandableCard(
    title: String,
    content:List<String>
){
    var expandedState by remember { mutableStateOf(false) }

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
                    var mainCheckState by remember { mutableStateOf(false) }
                    Row{
                        Checkbox(
                            modifier = Modifier.padding(bottom = 10.dp),
                            checked = mainCheckState,
                            onCheckedChange = { mainCheckState = it },
                        )
                        Text(text = " Seleccionar todos", fontSize = 16.sp)
                    }
                    for (element in content){
                        Row(
                            Modifier.padding(start = 20.dp)
                        ){
                            var checkState by remember { mutableStateOf(false) }
                            Checkbox(
                                modifier = Modifier.padding(bottom = 10.dp),
                                checked = checkState or mainCheckState,
                                onCheckedChange = { checkState = it },
                                colors = CheckboxDefaults.colors(colorResource(id = R.color.colorPrimary) )
                            )
                            Text(text = " $element", fontSize = 18.sp)
                        }
                    }
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth().height(1.dp),
                color = Color.Gray
            ) {}
        }
    }
}


@Composable
@Preview
fun DefaultPreview(){
    ExpandableCard(
        title = "Canal",
        content = listOf("Lorem","ipsum","dolor","sit amet")
    )
}