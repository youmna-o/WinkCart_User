package com.example.winkcart_user.data.local.settings

import kotlinx.coroutines.flow.Flow

interface SettingsDao {
    fun readCurrencyCode(): Flow<String>
    suspend fun writeCurrencyCode(currencyCode: String)

    fun readCurrencyRate(): Flow<String>
    suspend fun writeCurrencyRate(currencyRate: String)

    fun readCurrencyReadingDate(): Flow<String>
    suspend fun writeCurrencyReadingDate(currencyReadingDate: String)
}