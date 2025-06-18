package com.example.winkcart_user.cart.view

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.winkcart_user.R
import com.example.winkcart_user.cart.view.components.CartItem
import com.example.winkcart_user.cart.view.components.CouponItem
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.theme.BackgroundColor
import com.example.winkcart_user.ui.utils.components.CustomButton
import com.example.winkcart_user.ui.utils.Constants.SCREEN_PADDING
import kotlinx.coroutines.launch
import com.example.winkcart_user.cart.view.components.EmptyCart
import com.example.winkcart_user.ui.utils.components.LottieAnimationView


@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun CartView(viewModel: CartViewModel= hiltViewModel(), checkoutAction: (String,String,String,String) -> Unit, backAction: () -> Unit,navController: NavController) {
    val currencyCodeSaved by viewModel.currencyCode.collectAsState()
    val currencyRateSaved by viewModel.currencyRate.collectAsState()
    val draftOrders by viewModel.draftOrders.collectAsState()
    val customerID by viewModel.customerID.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val priceRules by viewModel.priceRules.collectAsState()

    val discount by viewModel.discountAmount.collectAsState()

    var promoCode by remember { mutableStateOf("") }
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()


    viewModel.refreshTotalAmount()
    viewModel.readCustomerID()
    viewModel.getDraftOrders(customerId = customerID)

    viewModel.readCurrencyRate()
    viewModel.readCurrencyCode()
    viewModel.getPriceRules()

    LaunchedEffect(showSheet) {
        if (showSheet) {
            coroutineScope.launch {
                sheetState.show()
            }
        }
    }

    val priceRulesList = when (priceRules) {
        is ResponseStatus.Success -> (priceRules as ResponseStatus.Success).result.price_rules
        else -> emptyList()
    }


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
            val result = orders.result.draft_orders

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.cart),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { backAction.invoke() }) {
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

                            if (result.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                ) {
                                    items(result.size) { index ->
                                        CartItem(
                                            draftOrder = result[index],
                                            currencyCode = currencyCodeSaved,
                                            currencyRate = currencyRateSaved,
                                            onDeleteClick = { draftOrderId ->
                                                viewModel.deleteDraftOrder(draftOrderId)
                                            },
                                            onQuantityChange = { updatedDraftOrder, newQuantity ->
                                                val updatedLineItem =
                                                    updatedDraftOrder.line_items[0]?.copy(quantity = newQuantity)

                                                val updatedDraftOrderRequest = DraftOrderRequest(
                                                    draft_order = result[index].copy(
                                                        line_items = listOf(
                                                            updatedLineItem
                                                        )
                                                    ),

                                                    )

                                                viewModel.refreshTotalAmount()

                                                viewModel.updateDraftOrder(
                                                    updatedDraftOrder.id,
                                                    updatedDraftOrderRequest
                                                )
                                            },
                                            onItemClick = {
                                                val productId =
                                                    result[index].line_items[0]?.product_id ?: 0L
                                                navController.navigate("ProductInfo/$productId")
                                            }
                                        )

                                    }
                                }
                                Spacer(Modifier.height(30.dp))
                                OutlinedTextField(
                                    value = promoCode,
                                    onValueChange = {
                                        promoCode = it

                                    },
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    label = { Text(stringResource(R.string.choose_your_promo_code)) },
                                    trailingIcon = {
                                        if (appliedCoupon != null) {
                                            // Show close icon
                                            Surface(
                                                shape = CircleShape,
                                                color = Color.Red,
                                                shadowElevation = 4.dp,
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        // Clear applied coupon
                                                        promoCode = ""
                                                        viewModel.clearAppliedCoupon()
                                                    },
                                                    modifier = Modifier.size(36.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Remove,
                                                        contentDescription = "Remove coupon",
                                                        tint = Color.White
                                                    )
                                                }
                                            }
                                        } else {
                                            // Show apply arrow icon
                                            Surface(
                                                shape = CircleShape,
                                                color = Color.Black,
                                                shadowElevation = 4.dp,
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        showSheet = true
                                                    },
                                                    modifier = Modifier.size(36.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.ArrowDropDown,
                                                        contentDescription = "Apply promo",
                                                        tint = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp)

                                )
                                /*   Spacer(Modifier.height(20.dp))*/

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.total_amount),
                                        color = Color.Gray,
                                        fontSize = 24.sp,
                                    )
                                    Text(
                                        text = totalAmount,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                    )

                                }

                                Spacer(Modifier.height(30.dp))
                                CustomButton(lable = stringResource(R.string.check_out)) {
                                    if (result.isNotEmpty()) {
                                        checkoutAction(totalAmount, currencyCodeSaved, promoCode, discount )
                                    }
                                }
                                Spacer(Modifier.height(100.dp))


                            }  else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(paddingValues),
                                    contentAlignment = Alignment.Center
                                ) {

                                    EmptyCart()
                                }


                            }


                        }

                    }
                }
            }
        }


    if (showSheet) {

        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()

            ) {
                Text(
                    text = stringResource(R.string.your_promo_codes),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )


                Spacer(modifier = Modifier.height(20.dp))

                if (priceRulesList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(priceRulesList.size) { index ->
                            val couponImage : Int = if(priceRulesList[index].value_type == "percentage"){
                                R.drawable.discount_copoun
                            }else{
                                R.drawable.receipt_copoun
                            }

                            CouponItem(
                                viewModel = viewModel,
                                priceRule = priceRulesList[index],
                                imageID = couponImage,
                                onApplyClicked = { selectedPriceRule, code ->
                                    promoCode = code
                                    showSheet = false
                                    viewModel.setAppliedCoupon(selectedPriceRule)
                                    viewModel.refreshTotalAmount()
                                },
                                currencyCode = currencyCodeSaved,
                                currencyRate = currencyRateSaved
                            )
                        }

                    }
                }


            }
        }
    }
}
