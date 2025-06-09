package com.example.winkcart_user.ui.productInfo.componants

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FavIcon( onClick:()-> Unit ){
    Surface(
        shape = CircleShape,
        color = Color.Black,
        shadowElevation = 4.dp,
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Add to favorites",
                tint = Color.Gray,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}