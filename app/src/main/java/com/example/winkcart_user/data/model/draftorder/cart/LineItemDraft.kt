package com.example.winkcart_user.data.model.draftorder.cart

data class LineItemDraft(
    val variant_id: Long,
    val product_id: Long?,
    val title: String,
    val price: String,
    val quantity: Int,
    val properties: List<Property>
)
