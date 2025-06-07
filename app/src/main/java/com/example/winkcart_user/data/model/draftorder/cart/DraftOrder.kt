package com.example.winkcart_user.data.model.draftorder.cart

data class DraftOrder(
    val line_items: List<LineItem?>,
    val customer: Customer
)