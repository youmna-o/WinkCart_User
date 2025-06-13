package com.example.winkcart_user.data.model.orders

import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft

data class OrderRequest(
    val order: OrderData
)

data class OrderData(
    val customer: CustomerOrder,
    val line_items: List<LineItemDraft>,
    val send_receipt: Boolean = true
)


data class CustomerOrder(
    val id: Long
)