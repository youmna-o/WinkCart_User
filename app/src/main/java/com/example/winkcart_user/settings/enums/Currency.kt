package com.example.winkcart_user.settings.enums

import androidx.annotation.StringRes
import com.example.winkcart_user.R

enum class Currency(@StringRes val stringResId: Int) {
    EGP(R.string.currency_egp),
    USD(R.string.currency_usd);

    companion object {
        fun getAll(): List<Currency> = entries
    }
}