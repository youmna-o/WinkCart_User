package com.example.winkcart_user.data.remote.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://mad45-sv-and2.myshopify.com/admin/api/2025-01/"
class RetrofitHelper {

    private var retrofit: Retrofit? =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    val apiServices = retrofit?.create(Services::class.java)



}