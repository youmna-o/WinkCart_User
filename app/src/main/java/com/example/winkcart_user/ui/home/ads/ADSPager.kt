package com.example.winkcart_user.ui.home.ads

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ADSPager() {
    val images = listOf(
        R.drawable.ads8,
        R.drawable.ads6,
        R.drawable.ads9,
        R.drawable.ads4
    )

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    // Auto-scroll every 2 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.scrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            // Pager
            HorizontalPager(
                count = images.size,
                state = pagerState,

                modifier = Modifier.wrapContentSize()
            ) { currentPage ->
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .height(200.dp),

                        //.padding(26.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(),

                        painter = painterResource(id = images[currentPage]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Right arrow
            IconButton(
                onClick = {
                    val next = pagerState.currentPage + 1
                    if (next < images.size) {
                        scope.launch { pagerState.scrollToPage(next) }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(48.dp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0x52373737))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight
                    ,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.LightGray
                )
            }

            // Left arrow
            IconButton(
                onClick = {
                    val prev = pagerState.currentPage - 1
                    if (prev >= 0) {
                        scope.launch { pagerState.scrollToPage(prev) }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .size(48.dp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0x52373737))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft
                    ,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.LightGray
                )
            }
        }

        // Page indicators
        PageIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}



@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount){
            IndicatorDots(isSelected = it == currentPage, modifier= modifier)
        }
    }
}

@Composable
fun IndicatorDots(isSelected: Boolean, modifier: Modifier) {
    val size = animateDpAsState(targetValue = if (isSelected) 12.dp else 10.dp, label = "")
    Box(modifier = modifier.padding(2.dp)
        .size(size.value)
        .clip(CircleShape)
        .background(if (isSelected) Color(0xff373737) else Color(0xA8373737))
    )
}