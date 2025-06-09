package com.example.winkcart_user.data.remote.retrofit

import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.data.model.customer.Customer
import com.example.winkcart_user.data.model.customer.CustomerResponse
import com.example.winkcart_user.data.model.customer.CustomerWrapper

import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse

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

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
    suspend fun createDraftOrder(
        @Header("X-Shopify-Access-Token") token: String,
        @Body draftOrderRequest: DraftOrderRequest
    ): Response<Any> // Replace Any with a response model if needed

    @GET("draft_orders.json")
    suspend fun getAllDraftOrders(
        @Header("X-Shopify-Access-Token") token: String,
    ): Response<DraftOrderResponse>

    @DELETE("draft_orders/{id}.json")
    suspend fun deleteDraftOrder(
        @Header("X-Shopify-Access-Token") token: String,
        @Path("id") draftOrderId: Long
    ): Response<Unit>

    @PUT("draft_orders/{id}.json")
    suspend fun updateDraftOrder(
        @Header("X-Shopify-Access-Token") token: String,
        @Path("id") draftOrderId: Long,
        @Body draftOrder: DraftOrderRequest
    ): Response<DraftOrderResponse>

    @GET("price_rules.json")
    suspend fun getPriceRules(
        @Header("X-Shopify-Access-Token") token: String
    ): Response<PriceRulesResponse>

}



interface CurrencyService {
    @GET("v3/latest")
    suspend fun getLatestRateFromUSDToEGP(
        @Query("apikey") apiKey: String,
        @Query("currencies") currencies: String
    ): Response<CurrencyResponse>
}