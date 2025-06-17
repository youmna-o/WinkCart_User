package com.example.winkcart_user.payment.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun SummarySection(
    orderAmount: String,
    discount: String,
    currencyCode: String,
    onClick: () -> Unit
) {
    val totalAfterDiscount = orderAmount.toDoubleOrNull() ?: 0.0
    val discountValue = discount.toDoubleOrNull() ?: 0.0
    val totalBeforeDiscount = (totalAfterDiscount + discountValue).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        // Total before discount
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total Before Discount:")
            Text(String.format("%.2f %s", totalBeforeDiscount, currencyCode))
        }

        // Discount
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Discount:")
            Text("-${String.format("%.2f %s", discountValue, currencyCode)}")
        }

        // Total after discount
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total to Pay:", fontWeight = FontWeight.Bold)
            Text(
                text = String.format("%.2f %s", totalAfterDiscount, currencyCode),
                fontWeight = FontWeight.Bold
            )
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

/*fun SummarySection(orderAmount: String, discount: String,currencyCode: String, onClick: () -> Unit) {
    val summary = orderAmount.toDouble() + discount.toDouble()
    val realPeice = orderAmount.toDouble()
    val discount = discount.toDouble()
    val formattedSummary:String?
    if(summary<0.0){
        formattedSummary = "0.00"
    }else{
        formattedSummary = String.format("%.2f", summary)
    }

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
            Text("$formattedSummary $currencyCode")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Discount:")
            Text("$discount $currencyCode")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Summary:", fontWeight = FontWeight.Bold)
            Text("$orderAmount $currencyCode", fontWeight = FontWeight.Bold)
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
}*/
