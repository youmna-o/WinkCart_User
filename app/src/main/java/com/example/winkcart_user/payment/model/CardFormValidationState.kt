package com.example.winkcart_user.payment.model

data class CardFormValidationState(
    val nameOnCartError: Boolean = false,
    val cardNumberError: Boolean = false,
    val expireDateError: Boolean = false,
    val cvvError: Boolean = false
)
