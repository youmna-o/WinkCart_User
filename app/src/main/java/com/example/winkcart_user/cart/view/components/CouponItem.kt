package com.example.winkcart_user.cart.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRule
import com.example.winkcart_user.theme.myPurple
import com.example.winkcart_user.ui.utils.CurrencyConversion.convertCurrency


@Composable
fun CouponItem(
    viewModel: CartViewModel,
    priceRule: PriceRule,
    imageID: Int,
    onApplyClicked: (PriceRule, String) -> Unit,
    currencyCode: String,
    currencyRate: String
) {
    LaunchedEffect(priceRule.id) {
        viewModel.getDiscountCodesByPriceRule(priceRule.id)
    }

    val discountCodesMap by viewModel.discountCodesByPriceRule.collectAsState()
    val discountCodeState = discountCodesMap[priceRule.id]

    val firstDiscountCode = remember { mutableStateOf("Loading...") }

    LaunchedEffect(discountCodeState) {
        when (discountCodeState) {
            is ResponseStatus.Success -> {
                val codes = discountCodeState.result.discount_codes
                firstDiscountCode.value = if (codes.isNotEmpty()) codes[0].code else "No code"
            }
            is ResponseStatus.Error -> {
                firstDiscountCode.value = "Error loading code"
            }
            is ResponseStatus.Loading, null -> {
                firstDiscountCode.value = "Loading..."
            }
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageID),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(80.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(end = 12.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = priceRule.title,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .width(110.dp),
                            softWrap = false
                        )

                        Text(
                            text = firstDiscountCode.value,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        val priceDiscount = convertCurrency(priceRule.value.trimStart('-'), currencyRate, currencyCode)
                        val discountText = when (priceRule.value_type) {
                            "percentage" -> "${priceRule.value.trimStart('-')}%"
                            "fixed_amount" -> {"$priceDiscount $currencyCode"}
                            else -> ""
                        }

                        Text(
                            text = discountText,
                            color = Color.Red,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier
                            .padding(end = 12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = viewModel.getRemainingMonthsText(priceRule.ends_at),
                            color = Color.Gray,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            shadowElevation = 12.dp,
                            color = Color.Transparent
                        ) {
                            Button(
                                onClick = {
                                    onApplyClicked(priceRule, firstDiscountCode.value)
                                },
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = myPurple),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(36.dp)
                            ) {
                                Text(
                                    text = "Apply",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}





