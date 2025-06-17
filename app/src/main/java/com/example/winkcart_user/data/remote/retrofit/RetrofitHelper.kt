package com.example.winkcart_user.data.remote.retrofit

import android.util.Log
import com.example.winkcart_user.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.winkcart_user.utils.Constants.BASE_URL_CURRENCY
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject


class RetrofitHelper @Inject constructor()  {

    val logging = HttpLoggingInterceptor(){ message ->
        Log.i("TAG", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
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