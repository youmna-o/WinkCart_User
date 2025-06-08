package com.example.winkcart_user.data.remote.retrofit

import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.data.model.customer.Customer
import com.example.winkcart_user.data.model.customer.CustomerResponse
import com.example.winkcart_user.data.model.customer.CustomerWrapper
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse

import com.example.winkcart_user.data.model.products.ProductResponse


import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface Services {
    @GET("smart_collections.json")
    suspend fun getSmartCollections( @Header("X-Shopify-Access-Token") token: String):Response<SmartCollectionsResponse>

    @GET("products.json?limit=250")
    suspend fun getAllProducts(
        @Header("X-Shopify-Access-Token") token: String
    ): Response<ProductResponse>



    @GET("products.json")
    suspend fun getProductsByVendor (
        @Header("X-Shopify-Access-Token") token: String,
        @Query("vendor") vendor: String
        ) : Response<ProductResponse>


    @POST("customers.json")
    suspend fun postCustomer(
        @Header("X-Shopify-Access-Token") token: String,
        @Body customerWrapper: CustomerWrapper
    ): Response<CustomerResponse>

    @POST("draft_orders.json")
    suspend fun postDraftOrder(
        @Header("X-Shopify-Access-Token") token: String,
        @Body customerWrapper: CustomerWrapper
    ): Response<CustomerResponse>



}



interface CurrencyService {
    @GET("v3/latest")
    suspend fun getLatestRateFromUSDToEGP(
        @Query("apikey") apiKey: String,
        @Query("currencies") currencies: String
    ): Response<CurrencyResponse>
}