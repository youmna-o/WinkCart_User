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
import com.example.winkcart_user.ui.theme.myPurple


@Composable
fun CouponItem(viewModel: CartViewModel, priceRule : PriceRule, imageID: Int, onApplyClicked: (PriceRule, String ) -> Unit) {

    LaunchedEffect(priceRule.id) {
        viewModel.getDiscountCodesByPriceRule(priceRule.id)
    }

    val discountCodeState by viewModel.priceRuleDiscountCodes.collectAsState()

    val firstDiscountCode = when (discountCodeState) {
        is ResponseStatus.Success -> {
            val codes = (discountCodeState as ResponseStatus.Success).result?.discount_codes
            if (codes?.isNotEmpty() == true) codes[0].code else "No code"
        }
        is ResponseStatus.Error -> "Error loading code"
        is ResponseStatus.Loading -> "Loading..."
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),

        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageID),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(65.dp)
                        .width(100.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 12.dp,
                                bottomStart = 12.dp
                            )
                        )
                )

                Spacer(Modifier.width(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = priceRule.title,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = firstDiscountCode,
                            color = Color.Gray,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }
                    Spacer(Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = viewModel.getRemainingMonthsText(
                                endsAt = priceRule.ends_at
                            ),
                            color = Color.Gray,
                            fontSize = 10.sp,
                        )
                        Spacer(Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            shadowElevation = 16.dp,
                            color = Color.Transparent
                        ) {
                            Button(
                                onClick = {
                                    onApplyClicked(priceRule,firstDiscountCode)

                                },
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = myPurple),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(30.dp)
                            ) {
                                Text(
                                    text = "Apply",
                                    color = Color.White,
                                    fontSize = 10.sp,
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






