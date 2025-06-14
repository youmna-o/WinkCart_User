package com.example.winkcart_user.ui.profile.orders.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.orders.Order
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersViewModel
import com.example.winkcart_user.ui.utils.formatDate

@Composable
fun OrderDetailsScreen (navController: NavController, ordersViewModel: OrdersViewModel,orderID :Long){

    ordersViewModel.getOrderDetails(orderID)
    val orderDetails = ordersViewModel.orderDetails.collectAsState().value
    when (orderDetails) {
        is ResponseStatus.Success<*> ->{
            OrderDetailsScreenOnSuccess((orderDetails as ResponseStatus.Success).result.order,navController)
        }
        is ResponseStatus.Loading ->{
            OrderDetailsScreenOnLoading()
        }
        is ResponseStatus.Error -> {
            OrderDetailsScreenOnError({ ordersViewModel.getOrderDetails(orderId = orderID) })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreenOnSuccess(order:Order,navController: NavController){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Orders",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(padding.calculateTopPadding()))
                Column {
                    Text("Order №${order.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Row( modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = formatDate(order.createdAt),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Delivered",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )                }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("${order.lineItems.size} items", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(order.lineItems.size) { index ->
                val product = order.lineItems[index]
                OrderItemCard(product.title,product.quantity,product.price,order.currency)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Order information", fontWeight = FontWeight.Bold, fontSize = 16.sp)
              //  OrderInfoRow("Shipping Address:", "${order.shippingAddress.city}, ${order.shippingAddress.country}")
                OrderInfoRow("Total Amount:", "${order.currentTotalPrice} ${order.currency}")
                OrderInfoRow("Discount:", "${order.currentTotalDiscounts} ${order.currency}" )
            }
        }

    }

}


@Composable
fun OrderDetailsScreenOnLoading(){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Loading Orders...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun OrderDetailsScreenOnError(onRetry: () -> Unit = {}){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}


