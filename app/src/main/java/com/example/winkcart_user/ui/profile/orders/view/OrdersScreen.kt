package com.example.winkcart_user.ui.profile.orders.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.winkcart_user.BottomNavigationBar
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.orders.Order
import com.example.winkcart_user.ui.categorie.categoriesViewModel.CategoriesViewModel
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersViewModel

@Composable
fun OrdersScreen (navController: NavController, ordersViewModel: OrdersViewModel){
    ordersViewModel.getUserOrders()
    var orders = ordersViewModel.ordersList.collectAsState().value
    when (orders) {
        is ResponseStatus.Success<*> ->{
            Log.i("TAG", "OrdersScreen: ${(orders as ResponseStatus.Success).result.orders.size}")
            OrdersScreenOnSucc(navController,(orders as ResponseStatus.Success).result.orders)
        }
        is ResponseStatus.Loading ->{
            OrdersScreenonLoading()
        }

        is ResponseStatus.Error -> {
            OrdersScreenOnError({ ordersViewModel.getUserOrders() })
        }
    }
}
/*@Composable
fun OrdersScreen(navController: NavController, ordersViewModel: OrdersViewModel) {
    val ordersState = ordersViewModel.ordersList.collectAsState().value

    when (ordersState) {
        is ResponseStatus.Success -> {
            val result = ordersState.result
            val orderList = result?.orders

            if (orderList != null) {
                Log.i("TAG", "OrdersScreen: ${orderList.size}")
                OrdersScreenOnSucc(navController, orderList)
            } else {
                // Null safety fallback if result or orders is null
                Log.i("TAG", "OrdersScreen: ${orderList?.size?:0}")
                OrdersScreenOnError(onRetry = { ordersViewModel.getUserOrders() })
            }
        }

        is ResponseStatus.Loading -> {
            OrdersScreenonLoading()
        }

        is ResponseStatus.Error -> {
            OrdersScreenOnError(onRetry = { ordersViewModel.getUserOrders() })
        }
    }
}*/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreenOnSucc(navController: NavController, orders : List<Order>) {
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
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    // Empty spacer to center the title
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController =navController
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(orders.size) { index ->
                OrderCard(order = orders[index])
            }
        }
    }
}

@Composable
fun OrdersScreenonLoading(){
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
fun OrdersScreenOnError(onRetry: () -> Unit = {}){
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