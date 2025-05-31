package com.example.winkcart_user.ui.productInfo.componants

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCard(url: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        GlideImage(
            model = url,
            contentDescription = "image",
            modifier = Modifier
                .fillMaxSize()
                .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )
    }
}
