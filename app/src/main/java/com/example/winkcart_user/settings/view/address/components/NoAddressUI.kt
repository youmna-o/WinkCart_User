package com.example.winkcart_user.settings.view.address.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.example.winkcart_user.ui.utils.components.LottieAnimationView

@Composable
fun NoAddressUI() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimationView(
            animationRes = R.raw.animation_empty_address
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.you_have_not_added_any_addresses_yet),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Gray
        )

        Text(
            text = stringResource(R.string.add_a_new_address_to_make_delivery_easier),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}