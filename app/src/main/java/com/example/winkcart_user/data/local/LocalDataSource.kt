package com.example.winkcart_user.data.local

import com.example.winkcart_user.settings.enums.Currency
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun readCurrencyCode(): Flow<String>
    suspend fun writeCurrencyCode(currencyCode: Currency)

    suspend fun readCurrencyRate(): Flow<String>
    suspend fun writeCurrencyRate(currencyRate: String)

    suspend fun readCurrencyReadingDate(): Flow<String>
    suspend fun writeCurrencyReadingDate(currencyReadingDate: String)

    suspend fun readCustomerID(): Flow<String>
    suspend fun writeCustomerID(customerID: String)

}