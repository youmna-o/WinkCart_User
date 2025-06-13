package com.example.winkcart_user.ui.checkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.winkcart_user.CurrencyViewModel
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrder
import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft
import com.example.winkcart_user.ui.checkout.view.viewModel.CheckoutViewModel
import com.example.winkcart_user.utils.CurrencyConversion.convertCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(cartViewModel: CartViewModel,currencyViewModel: CurrencyViewModel,
                   navController: NavController,/*checkoutViewModel: CheckoutViewModel*/) {

    val draftOrders by cartViewModel.draftOrders.collectAsState()
    var currencyCode = currencyViewModel.currencyCode.collectAsState().value
    var currencyRate= currencyViewModel.currencyRate.collectAsState().value
    val draftOrderList = when (draftOrders) {
        is ResponseStatus.Success -> (draftOrders as ResponseStatus.Success).result.draft_orders
        else -> emptyList()
    }
    Scaffold (    topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Checkout",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            ),

            )
    }){

        padding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            val totalAmount = draftOrderList.sumOf { order ->
                val item = order.line_items[0]
                val priceStr = item?.price ?: "0.0"
                val converted = convertCurrency(
                    amount = priceStr,
                    rate = currencyRate,
                    currencyCode = currencyCode
                )
                converted?.toDoubleOrNull() ?: 0.0
            }
            val allLineItems: List<LineItemDraft> = draftOrderList.flatMap { draftOrder ->
                draftOrder.line_items.mapNotNull { it }
            }


            item {

                Spacer(modifier = Modifier.height(90.dp))

                ShippingAddressSection()
                Spacer(modifier = Modifier.height(16.dp))

                PaymentSection()
                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(16.dp))

                ProductListSection(draftOrderList, currencyCode = currencyCode, currencyRate = currencyRate)
                Spacer(modifier = Modifier.height(16.dp))

                SummarySection(orderAmount = totalAmount, deliveryFee = 0,{/*checkoutViewModel.createOrder( lineItems = allLineItems )*/})
            }

        }
    }

}

@Composable
fun ShippingAddressSection() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Shipping address", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Jane Doe")
            Text("3 Newbridge Court\nChino Hills, CA 91709, United States")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Change",
                color = Color.Red,
                modifier = Modifier.clickable { }
            )
        }
    }
}

@Composable
fun PaymentSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Payment", fontWeight = FontWeight.Bold)
            Text("**** **** **** 3947")
        }
        Text("Change", color = Color.Red, modifier = Modifier.clickable { })
    }
}
@Composable
fun DeliveryOption(name: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(end = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(name)
            Text("2-3 days", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProductListSection(productList :List<DraftOrder>,currencyRate: String,currencyCode: String) {
    Column {
        Text("Products", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        productList.forEach { item->
            val price = item.line_items[0]?.let {
                convertCurrency(
                    amount = it.price,
                    rate = currencyRate,
                    currencyCode = currencyCode
                )
            }
            ProductItem(item.line_items[0]?.title.toString(),
                item.line_items[0]?.properties?.get(3)?.value.toString(), price)
        }
    }
}

@Composable
fun ProductItem(name: String, imageUrl: String, price: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Medium)
            Text("$${price}", color = Color.Gray)
        }
    }
}

@Composable
fun SummarySection(orderAmount: Double, deliveryFee: Int,onClick:() -> Unit ) {
    val summary = orderAmount + deliveryFee
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Order:")
            Text("$${String.format("%.2f", orderAmount)}")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Delivery:")
            Text("$${deliveryFee}")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Summary:", fontWeight = FontWeight.Bold)
            Text("$${String.format("%.2f", summary)}", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text("SUBMIT ORDER", color = Color.White)
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

