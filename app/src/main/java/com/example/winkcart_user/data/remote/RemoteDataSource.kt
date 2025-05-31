package com.example.winkcart_user.data.remote

import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getLatestRateFromUSDToEGP() : Flow<CurrencyResponse?>

}