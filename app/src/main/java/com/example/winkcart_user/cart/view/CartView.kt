package com.example.winkcart_user.cart.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING


@Composable
fun CartView(viewModel: CartViewModel) {

    val draftOrders by viewModel.draftOrders.collectAsState()

    Log.i("TAG", "CartView: draftOrders = $draftOrders")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(SCREEN_PADDING)
    ) {

    }

}
