package com.example.winkcart_user.data.model.vendors

import kotlinx.serialization.Serializable

@Serializable
data class SmartCollection(
    val title: String,
    val image: Image?
)


