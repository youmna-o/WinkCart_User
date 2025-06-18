package com.example.winkcart_user.payment.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrder
import com.example.winkcart_user.ui.utils.CurrencyConversion.convertCurrency

@Composable
fun ProductListSection(
    productList: List<DraftOrder>,
    currencyRate: String,
    currencyCode: String
) {
    Column {
        Text("Products", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        productList.forEach { item ->
            val lineItem = item.line_items[0]
            val price = lineItem?.let {
                convertCurrency(
                    amount = it.price,
                    rate = currencyRate,
                    currencyCode = currencyCode
                )
            }
            ProductItemCheckout(
                name = lineItem?.title.toString(),
                imageUrl = lineItem?.properties?.getOrNull(3)?.value.toString(),
                price = price,
                currencyCode = currencyCode
            )
        }
    }
}
