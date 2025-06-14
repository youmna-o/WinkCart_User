package com.example.winkcart_user.data.model.coupons.discount

data class DiscountCode(
    val id: Long,
    val price_rule_id: Long,
    val code: String,
    val usage_count: Int,
    val created_at: String,
    val updated_at: String
)
