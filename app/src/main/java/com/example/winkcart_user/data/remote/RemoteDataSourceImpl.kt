package com.example.winkcart_user.data.remote

import android.util.Log
import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.orders.OrderDetailsResponse
import com.example.winkcart_user.data.model.orders.OrdersResponse
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.retrofit.MockDataSource
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSourceImpl(val retrofitHelper: RetrofitHelper) : RemoteDataSource {
  var auth = Firebase.auth
    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        val result = retrofitHelper.shopifyService.getSmartCollections(token = BuildConfig.shopifyAccessToken)
            .body()
        return flowOf(result)
    }

    override suspend fun getLatestRateFromUSDToEGP(): Flow<CurrencyResponse?> {
        val result = retrofitHelper.currencyService.getLatestRateFromUSDToEGP(apiKey = "cur_live_QrLvRdrZGY3drQOEGuVYBm1QsaJa4hAsP0WgcHiJ","EGP").body()
        return flowOf(result)
    }
    override suspend fun getAllProducts(): Flow<ProductResponse?> {
        val result = retrofitHelper.shopifyService.getAllProducts(token = BuildConfig.shopifyAccessToken)?.body()
        return flowOf(result)
    }


    override suspend fun getProductsByVendor( vendor : String): Flow<ProductResponse?> {
        val  result = retrofitHelper.shopifyService.getProductsByVendor(token = BuildConfig.shopifyAccessToken , vendor = vendor)?.body()
        return flowOf(result)
    }


    override   fun getRate(): Float {
       return MockDataSource.getRate()
    }

    override  fun getReview(): String {
        return MockDataSource.getReview()
    }

    override fun signUpFireBase(email: String, password: String): Task<AuthResult> {
        return  return auth.createUserWithEmailAndPassword(email, password)
    }

    override fun signInFireBase(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun createDraftOrder(
        draftOrderRequest: DraftOrderRequest
    ): Flow<Any> = flow {
            val response = retrofitHelper.shopifyService.createDraftOrder(token = BuildConfig.shopifyAccessToken, draftOrderRequest)
            emit(response)
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrderResponse?> {
        val result =
            retrofitHelper.shopifyService.getAllDraftOrders(token = BuildConfig.shopifyAccessToken)
                .body()
        return flowOf(result)
    }


    override suspend fun deleteDraftOrder(draftOrderId: Long): Flow<Unit?>{
        val result =
            retrofitHelper.shopifyService.deleteDraftOrder(token = BuildConfig.shopifyAccessToken, draftOrderId = draftOrderId)
                .body()
        return flowOf(result)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrderRequest: DraftOrderRequest
    ): Flow<DraftOrderResponse?> = flow {
        val response = retrofitHelper
            .shopifyService.updateDraftOrder(
            token = BuildConfig.shopifyAccessToken,
            draftOrderId = draftOrderId,
            draftOrder = draftOrderRequest
        ).body()
        emit(response)
    }

    override suspend fun getPriceRules(): Flow<PriceRulesResponse?> {
        val result =
            retrofitHelper.shopifyService
                .getPriceRules(token = BuildConfig.shopifyAccessToken)
                .body()
        return flowOf(result)
    }

    override suspend fun getOrders(customerId: Long): Flow<OrdersResponse?> {
        val response =  retrofitHelper.shopifyService
            .getUserOrders(token = BuildConfig.shopifyAccessToken,
                customerId = customerId ).body()
        return flowOf(response)
    }

    override suspend fun getSpecificOrderDEtails(orderId: Long): Flow<OrderDetailsResponse?> {
        var response = retrofitHelper.shopifyService.getOrderDetails(
            BuildConfig.shopifyAccessToken,orderId).body()
        var result = retrofitHelper.shopifyService.getOrderDetails(
            BuildConfig.shopifyAccessToken,orderId)
        Log.i("TAG", "getSpecificOrderDEtails: ${result.raw()}")
        Log.i("TAG", "getSpecificOrderDEtails: ${result.body()}")
        return flowOf(response)

    }
}