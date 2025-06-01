package com.example.winkcart_user.data.local

import com.example.winkcart_user.data.local.settings.SettingsDao
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val settingsDao: SettingsDao) : LocalDataSource {
    override suspend fun readCurrencyCode(): Flow<String> {
        return settingsDao.readCurrencyCode()
    }

    override suspend fun writeCurrencyCode(currencyCode: String) {
        settingsDao.writeCurrencyCode(currencyCode)
    }

    override suspend fun readCurrencyRate(): Flow<String> {
       return settingsDao.readCurrencyRate()
    }

    override suspend fun writeCurrencyRate(currencyRate: String) {
        settingsDao.writeCurrencyRate(currencyRate)
    }

    override suspend fun readCurrencyReadingDate(): Flow<String> {
        return settingsDao.readCurrencyReadingDate()
    }

    override suspend fun writeCurrencyReadingDate(currencyReadingDate: String) {
        return settingsDao.writeCurrencyReadingDate(currencyReadingDate)
    }

}