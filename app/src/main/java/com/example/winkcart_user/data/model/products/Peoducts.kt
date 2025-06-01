package com.example.winkcart_user.data.model.products

import com.example.winkcart_user.data.model.proddata.Variant

data class Product(
    val id: Long,
    val title: String,
    val body_html: String,
    val vendor: String,
    val product_type: String,
    val created_at: String,
    val handle: String,
    val updated_at: String,
    val published_at: String,
    val template_suffix: String?,
    val published_scope: String,
    val tags: String,
    val status: String,
    val admin_graphql_api_id: String,
    val variants: List<Variant>,
    val options: List<Option>,
    val images: List<ProductImage>,
    val image: ProductImage?
)

