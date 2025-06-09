package com.example.winkcart_user.data.remote.retrofit

import com.example.winkcart_user.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.winkcart_user.utils.Constants.BASE_URL_CURRENCY


/*
const val BASE_URL = "https://mad45-sv-and2.myshopify.com/admin/api/2025-01/"
class RetrofitHelper {

    private var retrofit: Retrofit? =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiServices = retrofit?.create(Services::class.java)

    val currencyService: CurrencyService = getRetrofit(BASE_URL_CURRENCY)
        .create(CurrencyService::class.java)


}*/

object RetrofitHelper {

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val shopifyService: Services by lazy {
        createRetrofit(Constants.BASE_URL_SHOPIFY).create(Services::class.java)
    }

    val currencyService: CurrencyService by lazy {
        createRetrofit(Constants.BASE_URL_CURRENCY).create(CurrencyService::class.java)
    }


}