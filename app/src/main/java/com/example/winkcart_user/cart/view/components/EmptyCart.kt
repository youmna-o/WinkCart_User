package com.example.winkcart_user.cart.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.example.winkcart_user.ui.utils.components.LottieAnimationView

@Composable
fun EmptyCart() {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LottieAnimationView(
                animationRes = R.raw.animation_empty_cart,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.headlineSmall,
                color = androidx.compose.ui.graphics.Color.Gray
            )

            Text(
                text = "Add some products to get started!",
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }

}
