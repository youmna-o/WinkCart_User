package com.example.winkcart_user.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.winkcart_user.R
import com.example.winkcart_user.ui.profile.orders.view.LottieAnimationView

@Composable
fun NoInternetScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            LottieAnimationView(text ="No internet connection", row = R.raw.final_internet )
        }
    }
}
