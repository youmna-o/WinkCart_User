package com.example.winkcart_user.ui.utils.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddFAB(action : ()-> Unit) {
    Box (modifier = Modifier.fillMaxSize()){
        FloatingActionButton(

            modifier = Modifier
                .padding(16.dp)
                .padding(vertical = 100.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .shadow(elevation = 6.dp)
                .size(50.dp)
            ,containerColor = Color.Black



            ,onClick = {action()}
        ) {
            Text(text = "+",color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.W300)

        }
    }
}