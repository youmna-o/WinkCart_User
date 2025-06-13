package com.example.winkcart_user.data.remote


import com.example.winkcart_user.data.model.customer.Customer
import com.example.winkcart_user.data.model.customer.CustomerRequest
import com.example.winkcart_user.data.model.customer.CustomerResponse

import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.orders.OrderDetailsResponse
import com.example.winkcart_user.data.model.orders.OrderRequest
import com.example.winkcart_user.data.model.orders.OrdersResponse

import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RemoteDataSource {

    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getLatestRateFromUSDToEGP() : Flow<CurrencyResponse?>

    suspend fun getAllProducts (): Flow<ProductResponse?>

    suspend fun getProductsByVendor(vendor : String): Flow<ProductResponse?>


     fun getRate (): Float

    fun getReview (): String

    fun signUpFireBase(email: String,password: String) :Task<AuthResult>

    fun signInFireBase(email: String,password: String) : Task<AuthResult>

    fun postCustomer (customer: CustomerRequest): Flow<CustomerResponse?>

    fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult>


        suspend fun createDraftOrder(
        draftOrderRequest: DraftOrderRequest
    ): Flow<Any>


    suspend fun getAllDraftOrders(): Flow<DraftOrderResponse?>
    suspend fun deleteDraftOrder(draftOrderId: Long): Flow<Unit?>

    suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrderRequest: DraftOrderRequest
    ): Flow<DraftOrderResponse?>

    suspend fun getPriceRules(): Flow<PriceRulesResponse?>
    suspend fun getOrders(customerId:Long) :Flow<OrdersResponse?>
    suspend fun  getSpecificOrderDEtails(orderId:Long):Flow<OrderDetailsResponse?>
    suspend fun createOrder(orderRequest: OrderRequest):Flow<OrdersResponse?>

}