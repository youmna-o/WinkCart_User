package com.example.winkcart_user.data.model.orders

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("orders")
    var orders : List<Order>
)
