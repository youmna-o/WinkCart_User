package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepo {
    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getFilteredSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getAllProducts() : Flow<ProductResponse?>

    suspend fun getProductsByVendor(vendor : String) : Flow<ProductResponse?>

     fun getRate() : Float
     fun getReview(): String


}