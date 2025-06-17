package com.example.winkcart_user.ui.utils.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(lable: String, onClick: ()-> Unit){
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().height(48.dp),) {
        Text(lable)
    }
}