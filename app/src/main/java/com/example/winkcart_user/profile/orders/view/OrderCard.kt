package com.example.winkcart_user.profile.orders.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.data.model.orders.Order
import com.example.winkcart_user.ui.utils.calculateQuantities
import com.example.winkcart_user.ui.utils.formatDate
import com.example.winkcart_user.ui.utils.getDeliveryStatus

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    val deliveryStatus = getDeliveryStatus(order.createdAt ?: "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Order №${order.orderNumber}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = formatDate(order.createdAt ?: ""),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                if (deliveryStatus.showStatus) {
                    Text(
                        text = deliveryStatus.statusText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = deliveryStatus.statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OrderDetailRow("Quantity:", "${calculateQuantities(order.lineItems)} unit")
            OrderDetailRow(
                "Price Before Discount:",
                "${order.totalLineItemsPrice} ${order.currency}"
            )
            OrderDetailRow(
                "Discount:",
                "${order?.discountApplications?.getOrNull(0)?.value?.toDoubleOrNull() ?: 0.0} ${order.currency}"
            )

            OrderDetailRow(
                "Total Price:",
                "${order.totalLineItemsPrice.toDouble() - (order.discountApplications.getOrNull(0)?.value?.toDoubleOrNull() ?: 0.0)} ${order.currency}"
            )


            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Text(
                    text = "Details",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun OrderDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Gray,
        )
    }
}

