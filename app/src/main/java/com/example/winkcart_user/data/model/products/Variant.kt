package com.example.winkcart_user.data.model.proddata

data class Variant(
    val id: Long,
    val product_id: Long,
    val title: String,
    val price: String,
    val position: Int,
    val inventory_policy: String,
    val compare_at_price: String?,
    val option1: String?,
    val option2: String?,
    val option3: String?,
    val created_at: String,
    val updated_at: String,
    val taxable: Boolean,
    val barcode: String?,
    val fulfillment_service: String,
    val grams: Int,
    val inventory_management: String?,
    val requires_shipping: Boolean,
    val sku: String,
    val weight: Double,
    val weight_unit: String,
    val inventory_item_id: Long,
    val inventory_quantity: Int,
    val old_inventory_quantity: Int,
    val admin_graphql_api_id: String,
    val image_id: Long?
)

