package com.example.winkcart_user.ui.productInfo

import androidx.compose.foundation.ScrollState

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.ui.productInfo.componants.ImageSlider
import com.example.winkcart_user.ui.productInfo.componants.LongBasicDropdownMenu
import com.example.winkcart_user.ui.productInfo.componants.Reviews
import com.example.winkcart_user.ui.productInfo.componants.StarRatingBar
import com.example.winkcart_user.ui.theme.myPurple
import com.example.winkcart_user.ui.utils.CustomButton


@Composable
fun ProductInfo(navController: NavController, scrollState: ScrollState) {
    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {

        ImageSlider()
        Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
            LongBasicDropdownMenu(lable = "Size")
            LongBasicDropdownMenu(lable = "color")

        }
        Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
            Text("Title",  style = MaterialTheme.typography.titleLarge)
            Text("152$",style = MaterialTheme.typography.titleLarge)
        }

        StarRatingBar(3.0f)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),
              //  .clip(RoundedCornerShape(16.dp)),
               colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ){
          Text(" Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){\n" +
                  "            Text(\"Title\",  style = MaterialTheme.typography.titleLarge)\n" +
                  "            Text(\"152\$\",style = MaterialTheme.typography.titleLarge)\n" +
                  "        }\n" +
                  "\n" +
                  "        StarRatingBar(3.0f)\n" +
                  "        Card(\n" +
                  "            modifier = Modifier\n" +
                  "                .fillMaxWidth()\n" +
                  "                .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),\n" +
                  "              //  .clip(RoundedCornerShape(16.dp)),\n" +
                  "               colors = CardDefaults.cardColors(\n" +
                  "                containerColor = Color.White\n" +
                  "            )\n" +
                  "        ){")
        }
          Reviews()

        CustomButton(
            lable = "ADD To CART"
        ) { }


    }
}



