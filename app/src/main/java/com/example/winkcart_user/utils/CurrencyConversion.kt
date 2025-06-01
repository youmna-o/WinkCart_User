package com.example.winkcart_user.utils

object CurrencyConversion {

    fun convertUsdToEgp(usdAmount: Double, rate: Double): Double {
        return usdAmount * rate
    }

    fun convertEgpToUsd(egpAmount: Double, rate: Double): Double {
        return egpAmount / rate
    }

}