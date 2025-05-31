package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.local.LocalDataSource
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductRepoImpl ( private  val remoteDataSource: RemoteDataSource) : ProductRepo {


    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        return  remoteDataSource.getSmartCollections()
    }

    override suspend fun getLatestRateFromUSDToEGP(): Flow<CurrencyResponse?> {
        return  remoteDataSource.getLatestRateFromUSDToEGP()
    }

}