package com.example.winkcart_user.utils

object CurrencyConversion {

    fun convertCurrency(amount: String, rate: String, currencyCode: String): String {
        val amountDouble = amount.toDoubleOrNull()
        val rateDouble = rate.toDoubleOrNull()

        if (amountDouble == null || rateDouble == null) return ""
        val result = when (currencyCode.uppercase()) {
            "USD" -> amountDouble / rateDouble
            "EGP" -> amountDouble * 1
            else -> return ""
        }

        return String.format("%.2f", result)
    }

}