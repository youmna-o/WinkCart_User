package com.example.winkcart_user.data.local.settings

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import androidx.core.content.edit
import com.example.winkcart_user.settings.enums.Currency
import kotlin.math.log

class SettingsDaoImpl(private val sharedPreferences: SharedPreferences): SettingsDao {

    companion object {
        private const val CURRENCY_READING_DATE = "currency_reading_date"
        private const val CURRENCY_CODE = "currency_code"
        private const val CURRENCY_RATE = "currency_rate"
        private const val CUSTOMER_ID = "customer_id"
    }
    override fun readCurrencyCode(): Flow<String>  = flow {
        val currencyCode = sharedPreferences.getString(CURRENCY_CODE, Currency.EGP.name) ?: Currency.EGP.name
        emit(currencyCode)
    }

    override suspend fun writeCurrencyCode(currencyCode: Currency) {
        sharedPreferences.edit { putString(CURRENCY_CODE, currencyCode.name) }
    }

    override fun readCurrencyRate(): Flow<String> = flow  {
        val currencyRate = sharedPreferences.getString(CURRENCY_RATE, "49.76") ?: "49.76"
        emit(currencyRate)
    }

    override suspend fun writeCurrencyRate(currencyRate: String) {
        sharedPreferences.edit { putString(CURRENCY_RATE, currencyRate) }
    }

    override fun readCurrencyReadingDate(): Flow<String> = flow {
        val currencyReadingDate = sharedPreferences.getString(CURRENCY_READING_DATE, "2025-05-31") ?: "2025-05-31"
        emit(currencyReadingDate)
    }

    override suspend fun writeCurrencyReadingDate(currencyReadingDate: String) {
        sharedPreferences.edit { putString(CURRENCY_READING_DATE, currencyReadingDate).commit() }
    }

    override fun readCustomerID(): Flow<String> = flow{
        val customerID = sharedPreferences.getString(CUSTOMER_ID, "") ?: ""
        emit(customerID)
    }

    override fun readCustomersID(): String {
        val customerID = sharedPreferences.getString(CUSTOMER_ID, "") ?: ""
        Log.i("G", "readCustomersID:customerID ")
        return customerID
    }

    override suspend fun writeCustomerID(customerID: String) {
        sharedPreferences.edit { putString(CUSTOMER_ID, customerID) }
    }

}