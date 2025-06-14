package com.example.winkcart_user.settings.model

data class AddressFormValidationState(
    val titleError: Boolean = false,
    val governorateError: Boolean = false,
    val addressError: Boolean = false,
    val contactPersonError: Boolean = false,
    val phoneError: Boolean = false
)
