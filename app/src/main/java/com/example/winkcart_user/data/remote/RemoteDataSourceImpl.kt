package com.example.winkcart_user.data.remote

import android.util.Log
import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.data.model.customer.Customer
import com.example.winkcart_user.data.model.customer.CustomerRequest
import com.example.winkcart_user.data.model.customer.CustomerResponse
import com.example.winkcart_user.data.model.customer.CustomerWrapper
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
        val result = retrofitHelper.apiServices?.getSmartCollections(token = BuildConfig.shopifyAccessToken)?.body()
        return flowOf(result)
    }

    override suspend fun getLatestRateFromUSDToEGP(): Flow<CurrencyResponse?> {
        val result = retrofitHelper.currencyService.getLatestRateFromUSDToEGP(apiKey = "cur_live_QrLvRdrZGY3drQOEGuVYBm1QsaJa4hAsP0WgcHiJ","EGP").body()
        return flowOf(result)
    }
    override suspend fun getAllProducts(): Flow<ProductResponse?> {
        val result = retrofitHelper.apiServices?.getAllProducts(token = BuildConfig.shopifyAccessToken)?.body()
        return flowOf(result)
    }


    override suspend fun getProductsByVendor( vendor : String): Flow<ProductResponse?> {
        val  result = retrofitHelper.apiServices?.getProductsByVendor(token = BuildConfig.shopifyAccessToken , vendor = vendor)?.body()
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

    override fun postCustomer(customer: CustomerRequest): Flow<CustomerResponse?> = flow {
        val wrapped = CustomerWrapper(customer)
        try {
            val response = retrofitHelper.apiServices?.postCustomer(
                token = BuildConfig.shopifyAccessToken,
                customerWrapper = wrapped
            )
            if (response != null && response.isSuccessful) {
                emit(response.body())
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }



}