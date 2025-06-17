package com.example.winkcart_user.data.model.orders

import com.google.gson.annotations.SerializedName

data class OrdersResponse(
    @SerializedName("orders")
    var orders: Order
)
