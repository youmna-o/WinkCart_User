package com.example.winkcart_user.data.model.settings.address

import com.google.gson.annotations.SerializedName

data class CustomerAddress (
    @SerializedName("id")
    val id : Long = 0,
    @SerializedName("company")
    val title: String,
    val country: String,
    val city: String,
    @SerializedName("address1")
    val address: String,
    val name: String,
    val phone: String,
    val default: Boolean = false
)
