package com.example.winkcart_user.data.model.draftorder.cart

data class DraftOrder(
    val id : Long = 0,
    val line_items: List<LineItemDraft?>,
    val customer: Customer
)