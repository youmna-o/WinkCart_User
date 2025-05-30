package com.example.winkcart_user.data.remote

import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?>

    suspend fun getAllProducts (): Flow<ProductResponse?>

    suspend fun getProductsByVendor(vendor : String): Flow<ProductResponse?>

    suspend fun getRate (): Double

    suspend fun getReview (): String



}