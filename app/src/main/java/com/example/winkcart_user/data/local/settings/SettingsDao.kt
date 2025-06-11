package com.example.winkcart_user.data.local.settings

import com.example.winkcart_user.settings.enums.Currency
import kotlinx.coroutines.flow.Flow

interface SettingsDao {
    fun readCurrencyCode(): Flow<String>
    suspend fun writeCurrencyCode(currencyCode: Currency)

    fun readCurrencyRate(): Flow<String>
    suspend fun writeCurrencyRate(currencyRate: String)

    fun readCurrencyReadingDate(): Flow<String>
    suspend fun writeCurrencyReadingDate(currencyReadingDate: String)

    fun readCustomerID(): Flow<String>
    suspend fun writeCustomerID(customerID: String)
}