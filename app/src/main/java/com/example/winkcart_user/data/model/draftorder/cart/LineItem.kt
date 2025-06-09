package com.example.winkcart_user.data.model.draftorder.cart

data class LineItem(
    val variant_id: Long,
    val title: String,
    val price: String,
    val quantity: Int,
    val properties: List<Property>
)
