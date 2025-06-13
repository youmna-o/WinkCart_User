package com.example.winkcart_user.data.remote.retrofit

import com.example.winkcart_user.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.winkcart_user.utils.Constants.BASE_URL_CURRENCY


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