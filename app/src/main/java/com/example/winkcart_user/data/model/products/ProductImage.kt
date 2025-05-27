package com.example.winkcart_user.data.model.products

data class ProductImage(
    val id: Long,
    val alt: String,
    val position: Int,
    val product_id: Long,
    val created_at: String,
    val updated_at: String,
    val admin_graphql_api_id: String,
    val width: Int,
    val height: Int,
    val src: String,
    val variant_ids: List<Long>
)
