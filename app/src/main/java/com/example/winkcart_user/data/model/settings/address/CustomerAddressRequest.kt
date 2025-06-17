package com.example.winkcart_user.data.model.settings.address

import com.google.gson.annotations.SerializedName

data class CustomerAddressRequest(
    @SerializedName("customer_address" )
    val customerAddress : CustomerAddress
)
