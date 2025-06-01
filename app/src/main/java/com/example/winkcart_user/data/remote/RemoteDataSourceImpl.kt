package com.example.winkcart_user.data.remote

import android.util.Log
import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.google.android.gms.tasks.Task

import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random

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

        var rate =  Random.nextDouble(from = 2.5, until =5.0 )
        return  rate.toFloat()
    }

    override  fun getReview(): String {
        var reviewList = listOf("Great product, super soft material!","Size fits perfectly, but the price is a bit high.","The color is vibrant, looks better than the pictures",
        "Decent quality, though the material feels slightly thin.","Love the color, but the size runs a bit small.","Good product, but the price could be lower.",
            "Awesome value for the price, sturdy material.","The fit is spot-on, and the color pops!")
        return reviewList.random()

    }

    override fun signUpFireBase(email: String, password: String): Task<AuthResult> {
        return  return auth.createUserWithEmailAndPassword(email, password)
    }

    override fun signInFireBase(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }



}