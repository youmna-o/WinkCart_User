package com.example.winkcart_user.data.model.customer


data class CustomerWrapper(
    val customer: CustomerRequest
)

data class CustomerRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String? = null,
    val verified_email: Boolean = false,
    val addresses: List<AddressRequest>? = null,
    val password: String,
    val password_confirmation: String,
    val send_email_welcome: Boolean = false
)
