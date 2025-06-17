package com.example.winkcart_user.data.model.orders

import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft

data class OrderRequest(
    val order: OrderData
)

data class OrderData(
    val customer: CustomerOrder,
    val line_items: List<LineItemDraft>,
    val discount_codes: List<DiscountCode>? = null,
    val send_receipt: Boolean = true
)


data class CustomerOrder(
    val id: Long
)
data class DiscountCode(
    val code: String,
    val amount: String,
    val type: String = "fixed_amount"
)
