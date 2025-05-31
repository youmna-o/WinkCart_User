package com.example.winkcart_user.data.remote

import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random

class RemoteDataSourceImpl(val retrofitHelper: RetrofitHelper) : RemoteDataSource {

    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        val result = retrofitHelper.apiServices?.getSmartCollections(token = BuildConfig.shopifyAccessToken)?.body()
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

    override  suspend fun getRate(): Double{
        var rate =  Random.nextDouble(from = 2.5, until =5.0 )
        return  round(rate * 10) / 10
    }

    override suspend fun getReview(): String {
        var reviewList = listOf("Great product, super soft material!","Size fits perfectly, but the price is a bit high.","The color is vibrant, looks better than the pictures",
        "Decent quality, though the material feels slightly thin.","Love the color, but the size runs a bit small.","Good product, but the price could be lower.",
            "Awesome value for the price, sturdy material.","The fit is spot-on, and the color pops!")
        return reviewList.random()

    }


}