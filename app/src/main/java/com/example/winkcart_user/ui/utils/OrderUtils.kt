package com.example.winkcart_user.ui.utils

import androidx.compose.ui.graphics.Color
import com.example.winkcart_user.data.model.orders.LineItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun calculateQuantities(lineItems: List<LineItem>): Int {
        return lineItems.sumOf { it.quantity }
}
fun calculateTotalAfterDiscount(lineItems: List<LineItem>): Double {
    return lineItems.sumOf { item ->
        val price = item.price.toDoubleOrNull() ?: 0.0
        val discount = item.totalDiscount.toDoubleOrNull() ?: 0.0
        (price - discount) * item.quantity
    }
}

fun formatDate(isoDateTime: String): String {
    return java.time.LocalDate.parse(isoDateTime.substringBefore('T'))
        .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy"))
}
data class DeliveryStatus(
    val showStatus: Boolean,
    val statusText: String,
    val statusColor: Color
)

fun getDeliveryStatus(createdAt: String): DeliveryStatus {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val creationDate = dateFormat.parse(createdAt)

        if (creationDate != null) {
            val currentTime = Date()
            val timeDifference = currentTime.time - creationDate.time
            val hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifference)

            when {
                hoursDifference < 24 -> DeliveryStatus(
                    showStatus = false,
                    statusText = "",
                    statusColor = Color.Transparent
                )
                else -> DeliveryStatus(
                    showStatus = true,
                    statusText = "Delivered",
                    statusColor = Color(0xFF4CAF50)
                )
            }
        } else {
            DeliveryStatus(
                showStatus = false,
                statusText = "",
                statusColor = Color.Transparent
            )
        }
    } catch (e: Exception) {
        DeliveryStatus(
            showStatus = false,
            statusText = "",
            statusColor = Color.Transparent
        )
    }
}

fun extractUsername(email: String): String {
    return email.substringBefore("@")
}


