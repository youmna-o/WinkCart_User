package com.example.winkcart_user.ui.productInfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.ui.productInfo.componants.ImageSlider
import com.example.winkcart_user.ui.productInfo.componants.LongBasicDropdownMenu
import com.example.winkcart_user.ui.theme.myPurple


@Composable
fun ProductInfo(navController: NavController, scrollState: ScrollState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        ImageSlider()
        Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
            LongBasicDropdownMenu(lable = "Size")
            LongBasicDropdownMenu(lable = "color")
        }
        Box(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
                .background(color = myPurple)
        ) { Text("ll") }
        Box(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .background(color = Color.White)
        ) { Text("ll") }
        Box(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
                .background(color = myPurple)
        ) { Text("ll") }
    }
}


