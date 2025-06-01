package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.local.LocalDataSource
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductRepoImpl ( private  val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource) : ProductRepo {


    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        return  remoteDataSource.getSmartCollections()
    }

    override suspend fun getLatestRateFromUSDToEGP(): Flow<CurrencyResponse?> {
        return  remoteDataSource.getLatestRateFromUSDToEGP()
    }

    override suspend fun readCurrencyCode(): Flow<String> {
        return localDataSource.readCurrencyCode()
    }

    override suspend fun writeCurrencyCode(currencyCode: String) {
        localDataSource.writeCurrencyCode(currencyCode)
    }

    override suspend fun readCurrencyRate(): Flow<String> {
        return localDataSource.readCurrencyRate()
    }

    override suspend fun writeCurrencyRate(currencyRate: String) {
        return localDataSource.writeCurrencyRate(currencyRate)
    }

    override suspend fun readCurrencyReadingDate(): Flow<String> {
        return localDataSource.readCurrencyReadingDate()
    }

    override suspend fun writeCurrencyReadingDate(currencyReadingDate: String) {
        localDataSource.writeCurrencyReadingDate(currencyReadingDate)
    }

}