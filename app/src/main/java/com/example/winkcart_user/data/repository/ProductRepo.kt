package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse

import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepo {
    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getLatestRateFromUSDToEGP(): Flow<CurrencyResponse?>

    suspend fun readCurrencyCode(): Flow<String>
    suspend fun writeCurrencyCode(currencyCode: String)

    suspend fun readCurrencyRate(): Flow<String>
    suspend fun writeCurrencyRate(currencyRate: String)

    suspend fun readCurrencyReadingDate(): Flow<String>
    suspend fun writeCurrencyReadingDate(currencyReadingDate: String)
    suspend fun getFilteredSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getAllProducts() : Flow<ProductResponse?>

    suspend fun getProductsByVendor(vendor : String) : Flow<ProductResponse?>

     fun getRate() : Float
     fun getReview(): String


}