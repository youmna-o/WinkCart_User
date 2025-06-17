package com.example.winkcart_user.favourite.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.R
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.favourite.FavouriteViewModel
import com.example.winkcart_user.favourite.view.components.EmptyFavorite
import com.example.winkcart_user.favourite.view.components.FavItem
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.utils.components.LottieAnimationView
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favourite(viewModel: FavouriteViewModel, navController: NavController) {

    val currencyCodeSaved by viewModel.currencyCode.collectAsState()
    val currencyRateSaved by viewModel.currencyRate.collectAsState()
    val draftOrders by viewModel.draftOrders.collectAsState()
    val customerID by viewModel.customerID.collectAsState()
    val scroll = rememberScrollState()


    LaunchedEffect(customerID) {
        viewModel.getDraftOrders(customerId = customerID)
    }

    viewModel.readCurrencyRate()
    viewModel.readCurrencyCode()

    when (val orders = draftOrders) {
        is ResponseStatus.Loading -> {
            LottieAnimationView(
                animationRes = R.raw.animation_loading,
                message = "Loading your cart..."
            )
            return
        }

        is ResponseStatus.Error -> {
            LottieAnimationView(
                animationRes = R.raw.animation_loading,
                message = "No internet connection"
            )
            return
        }

        is ResponseStatus.Success -> {
            val draftOrderList = orders.result.draft_orders

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Favourites",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundColor)
                        .padding(SCREEN_PADDING)
                        .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scroll)

                    ) {
                        if (draftOrderList.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                            ) {
                                items(draftOrderList.size) { index ->
                                    FavItem(
                                        draftOrder = draftOrderList[index],
                                        currencyCode = currencyCodeSaved,
                                        currencyRate = currencyRateSaved,
                                        onDeleteClick = { draftOrderId ->
                                            viewModel.deleteDraftOrder(draftOrderId)
                                        },
                                        onItemClick = {
                                            val productId =
                                                draftOrderList[index].line_items[0]?.product_id
                                                    ?: 0L
                                            navController.navigate("ProductInfo/$productId")
                                        }

                                    )

                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                contentAlignment = Alignment.Center
                            ) {

                                EmptyFavorite()
                            }
                        }

                        Spacer(Modifier.height(30.dp))


                    }

                }
            }
        }

    }
}

