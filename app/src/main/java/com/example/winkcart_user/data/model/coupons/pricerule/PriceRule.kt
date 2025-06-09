package com.example.winkcart_user.data.model.coupons.pricerule

data class PriceRule(
    val id: Long,
    val value_type: String,
    val value: String,
    val usage_limit: Int?,
    val starts_at: String,
    val ends_at: String,

    val title: String,
)
