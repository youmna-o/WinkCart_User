package com.example.winkcart_user.data.remote

import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSourceImpl(val retrofitHelper: RetrofitHelper) : RemoteDataSource {

    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        val result = retrofitHelper.apiServices?.getSmartCollections(token = BuildConfig.shopifyAccessToken)?.body()
        return flowOf(result)
    }

    override suspend fun getAllProducts(): Flow<ProductResponse?> {
        val result = retrofitHelper.apiServices?.getAllProducts(token = BuildConfig.shopifyAccessToken)?.body()
        return flowOf(result)
    }


}