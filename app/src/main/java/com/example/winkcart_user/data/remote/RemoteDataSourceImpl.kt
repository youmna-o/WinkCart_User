package com.example.winkcart_user.data.remote

import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.settings.address.CustomerAddress
import com.example.winkcart_user.data.model.settings.address.CustomerAddressRequest
import com.example.winkcart_user.data.model.settings.address.CustomerAddressesResponse
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
    ): Flow<DraftOrderResponse?> {
        val response = retrofitHelper.shopifyService.updateDraftOrder(
            token = BuildConfig.shopifyAccessToken,
            draftOrderId = draftOrderId,
            draftOrder = draftOrderRequest
        ).body()
        return flowOf(response)
    }

    override suspend fun getPriceRules(): Flow<PriceRulesResponse?> {
        val result =
            retrofitHelper.shopifyService.getPriceRules(token = BuildConfig.shopifyAccessToken)
                .body()
        return flowOf(result)
    }

    override suspend fun addCustomerAddress(
        customerId: Long,
        customerAddressRequest: CustomerAddressRequest
    ): Flow<Any> {
        val response = retrofitHelper.shopifyService.addCustomerAddress(
            token = BuildConfig.shopifyAccessToken,
            customerId = customerId,
            request = customerAddressRequest
        )
        return flowOf(response)
    }

    override suspend fun getCustomerAddresses(customerId: Long): Flow<CustomerAddressesResponse?> {
        val response = retrofitHelper.shopifyService.getCustomerAddresses(
            token = BuildConfig.shopifyAccessToken,
            customerId = customerId
        ).body()
        return flowOf(response)
    }

    override suspend fun setDefaultAddress(customerId: Long, addressId: Long): Flow<Unit?> {
        val result =
            retrofitHelper.shopifyService.setDefaultAddress(
                token = BuildConfig.shopifyAccessToken,
                customerId = customerId,
                addressId = addressId
            ).body()
        return flowOf(result)
    }

    override suspend fun getCustomerAddress(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddressRequest?> {
        val response =
            retrofitHelper.shopifyService.getCustomerAddress(
                token = BuildConfig.shopifyAccessToken,
                customerId = customerId,
                addressId = addressId
            ).body()
        return flowOf(response)
    }

    override suspend fun updateCustomerAddress(
        customerId: Long,
        addressId: Long,
        customerAddressRequest: CustomerAddressRequest
    ): Flow<Any?> {
        val response =
            retrofitHelper.shopifyService.updateCustomerAddress(
                token = BuildConfig.shopifyAccessToken,
                customerId = customerId,
                addressId = addressId,
                addressUpdateRequest = customerAddressRequest
            ).body()
        return flowOf(response)
    }

}