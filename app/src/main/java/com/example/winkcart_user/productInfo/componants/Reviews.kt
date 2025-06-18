package com.example.winkcart_user.productInfo.componants

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
fun Reviews(review: String , rate: Float, starSize: Float){
    Spacer(Modifier.height(16.dp))
    Text("Reviews", style = MaterialTheme.typography.titleLarge)
    LazyRow (horizontalArrangement = Arrangement.spacedBy(8.dp),){items (3){item -> ReviewCard(review,rate,starSize)  }  }
    Spacer(Modifier.height(16.dp))
}

@Composable
fun ReviewCard(review: String , rate: Float ,starSize: Float ){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ){
        Column (Modifier.padding(8.dp)){
            StarRatingBar(
                rating = rate,
                maxStars = 5,
                size = starSize
            )
            Text(review)
        }

    }
}