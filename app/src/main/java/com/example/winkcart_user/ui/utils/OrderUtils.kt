package com.example.winkcart_user.ui.utils

import com.example.winkcart_user.data.model.orders.LineItem

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

