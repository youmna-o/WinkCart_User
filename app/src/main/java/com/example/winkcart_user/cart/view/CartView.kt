package com.example.winkcart_user.cart.view

import android.util.Log
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
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.winkcart_user.R
import com.example.winkcart_user.cart.view.components.CartItem
import com.example.winkcart_user.cart.view.components.CouponItem
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.utils.CustomButton
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartView(viewModel: CartViewModel, checkoutAction: () -> Unit) {

    val currencyCodeSaved by viewModel.currencyCode.collectAsState()
    val currencyRateSaved by viewModel.currencyRate.collectAsState()
    val draftOrders by viewModel.draftOrders.collectAsState()
    val customerID by viewModel.customerID.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val priceRules by viewModel.priceRules.collectAsState()

    var promoCode by remember { mutableStateOf("") }
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()


    val couponImages = listOf(
        R.drawable.coupon3,
        R.drawable.coupon2,
        R.drawable.coupon1
    )

    LaunchedEffect(showSheet) {
        if (showSheet) {
            coroutineScope.launch {
                sheetState.show()
            }
        }
    }


    viewModel.refreshTotalAmount()
    viewModel.readCustomerID()
    viewModel.getDraftOrders(customerId = customerID)

    viewModel.readCurrencyRate()
    viewModel.readCurrencyCode()
    viewModel.getPriceRules()

    val draftOrderList = when (draftOrders) {
        is ResponseStatus.Success -> (draftOrders as ResponseStatus.Success).result.draft_orders
        else -> emptyList()
    }
    val priceRulesList = when (priceRules) {
        is ResponseStatus.Success -> (priceRules as ResponseStatus.Success).result.price_rules
        else -> emptyList()
    }

    Log.i("TAG", "CartView: draftOrders = ${draftOrderList.size}")
    Log.i("TAG", "CartView: priceRules = $priceRulesList")


    // Modal Bottom Sheet
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
                        modifier = Modifier.weight(1f) ,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(priceRulesList.size) { index ->
                            CouponItem(
                                viewModel = viewModel,
                                priceRule = priceRulesList[index],
                                imageID = couponImages[index],
                                onApplyClicked = { selectedPriceRule ->
                                    promoCode = selectedPriceRule.title
                                    showSheet = false
                                    viewModel.setAppliedCoupon(selectedPriceRule)
                                    viewModel.refreshTotalAmount()
                                }
                            )
                        }

                    }
                }


            }
        }
    }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(SCREEN_PADDING)
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
                            CartItem(
                                draftOrder = draftOrderList[index],
                                currencyCode = currencyCodeSaved,
                                currencyRate = currencyRateSaved,
                                onDeleteClick = { draftOrderId ->
                                    viewModel.deleteDraftOrder(draftOrderId)
                                },
                                onQuantityChange = { updatedDraftOrder, newQuantity ->
                                    val updatedLineItem =
                                        updatedDraftOrder.line_items[0]?.copy(quantity = newQuantity)

                                    val updatedDraftOrderRequest = DraftOrderRequest(
                                        draft_order = draftOrderList[index].copy(
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
                                }
                            )

                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        //need update with lotti or image for empty cart
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "  All products from this vendor are currently out of stock. ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                " Stay tuned for updates!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }


                    }
                }

                Spacer(Modifier.height(30.dp))


                OutlinedTextField(
                    value = promoCode,
                    onValueChange = {
                        promoCode = it

                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.Enter_your_promo_code)) },
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
                                        viewModel.clearAppliedCoupon() // call function to clear in ViewModel
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Remove, // or Icons.Default.Close
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
                                        imageVector = Icons.Outlined.ArrowForward,
                                        contentDescription = "Apply promo",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                    ,
                    shape = RoundedCornerShape(12.dp)

                )
                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
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
                    checkoutAction()
                }
                Spacer(Modifier.height(150.dp))
            }

        }

    }

