package com.example.winkcart_user.payment.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.CurrencyViewModel
import com.example.winkcart_user.R
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft
import com.example.winkcart_user.payment.view.components.DefaultAddressSection
import com.example.winkcart_user.payment.view.components.PaymentSection
import com.example.winkcart_user.payment.view.components.ProductListSection
import com.example.winkcart_user.payment.view.components.SummarySection
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.ui.checkout.view.viewModel.PaymentViewModel
import com.example.winkcart_user.ui.utils.components.PlayOnceThenHideAnimation
import com.example.winkcart_user.ui.utils.components.TimedLottieAnimation
import com.example.winkcart_user.ui.utils.navigation.NavigationRout
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    currencyViewModel: CurrencyViewModel,
    navController: NavController,
    paymentViewModel: PaymentViewModel,
    settingsViewModel: SettingsViewModel,
    cardNumber : String,
    totalAmount: String,
    currencyCode: String,
    goToSuccess: () -> Unit

) {
    val draftOrders by cartViewModel.draftOrders.collectAsState()
    val currencyRate = currencyViewModel.currencyRate.collectAsState().value
    val orderResponse = paymentViewModel.ordersResponse.collectAsState()
    val defaultCustomerAddress by settingsViewModel.defaultCustomerAddresses.collectAsState()
    val customerId by cartViewModel.customerID.collectAsState()
    val discount by cartViewModel.discountAmount.collectAsState()
    val discountOnly = discount.split(" ").first().toDoubleOrNull() ?: 0.0
    var showLottieCheckVerify by remember { mutableStateOf(false) }
    var showLottieVerified by remember { mutableStateOf(false) }
    Log.i("TAG", "CheckoutScreen: $discountOnly")

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    cartViewModel.readCustomerID()


    settingsViewModel.getCustomerAddresses(customerId.toLong())
    LaunchedEffect(orderResponse.value) {
        when (val response = orderResponse.value) {
            is ResponseStatus.Success -> showSuccessDialog = true
            is ResponseStatus.Error -> {
                errorMessage = response.error.message ?: "Unknown error"
                showErrorDialog = true
            }

            else -> {}
        }
    }

    val draftOrderList = when (draftOrders) {
        is ResponseStatus.Success -> (draftOrders as ResponseStatus.Success).result.draft_orders
        else -> emptyList()
    }


    val allLineItems: List<LineItemDraft> = draftOrderList.flatMap { draftOrder ->
        draftOrder.line_items.mapNotNull { it }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Checkout", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(SCREEN_PADDING)
                        .padding(padding)
                ) {
                    item {
                        when (val response = defaultCustomerAddress) {
                            is ResponseStatus.Success -> {
                                DefaultAddressSection(response.result)
                            }

                            is ResponseStatus.Error -> {
                                Text(
                                    text = "Add Address",
                                    color = Color.Red,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable { navController.navigate(NavigationRout.AddAddress.route) }
                                )
                            }

                            is ResponseStatus.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PaymentSection(
                            cardNumber = cardNumber,
                            changeAction = { navController.popBackStack() })
                        Spacer(modifier = Modifier.height(16.dp))
                        ProductListSection(
                            draftOrderList,
                            currencyCode = currencyCode,
                            currencyRate = currencyRate
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SummarySection(
                            orderAmount = totalAmount,
                            discount = discountOnly.toString(),
                            currencyCode = currencyCode
                        ) {
                            showLottieCheckVerify = true

                        }
                    }
                }
            }
        }

        if (showLottieCheckVerify) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                TimedLottieAnimation(
                    resId = R.raw.animation_loading,
                    durationMillis = 3000L,
                    message = "Verifying your order..."
                )
            }
            LaunchedEffect(showLottieCheckVerify) {
                if (showLottieCheckVerify) {
                    delay(3000)
                    showLottieCheckVerify = false
                    showLottieVerified = true
                }
            }
        }

        if (showLottieVerified) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                PlayOnceThenHideAnimation(resId = R.raw.verified_animation, message = "Done!") {

                    paymentViewModel.createOrder(lineItems = allLineItems)

                    draftOrderList.forEach { draftOrder ->
                        draftOrder.id.let { cartViewModel.deleteDraftOrder(it) }
                    }
                    showLottieVerified = false
                    goToSuccess()
                }

            }

        }

    }
}







