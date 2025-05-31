package com.example.winkcart_user.ui.productInfo.componants

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Reviews(){
    Spacer(Modifier.height(16.dp))
    Text("Reviews", style = MaterialTheme.typography.titleLarge)
    LazyRow (horizontalArrangement = Arrangement.spacedBy(8.dp),){items (3){item -> ReviewCard()  }  }
    Spacer(Modifier.height(16.dp))
}

@Composable
fun ReviewCard(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),
        //  .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ){
        Column (Modifier.padding(4.dp)){
            StarRatingBar(3.0f)
            Text("rrrrrrrrrrrrrr")
        }

    }
}