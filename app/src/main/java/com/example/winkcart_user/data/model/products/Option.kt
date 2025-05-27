package com.example.winkcart_user.data.model.products

data class Option(
    val id: Long,
    val product_id: Long,
    val name: String,
    val position: Int,
    val values: List<String>
)
