package com.example.winkcart_user.productInfo.componants

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.theme.myPurple
@Composable
fun ImageSlider(images: List<String>) {
    val pagerState = rememberPagerState{images.size}


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(

            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            ImageCard(images[page])
        }

        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                val isSelected = pagerState.currentPage == index
                val animatedSize = animateFloatAsState(if (isSelected) 12f else 8f)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(animatedSize.value.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) myPurple else Color.Gray.copy(alpha = 0.5f))
                )
            }
        }
    }
}
