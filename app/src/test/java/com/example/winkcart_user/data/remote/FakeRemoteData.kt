package com.example.winkcart_user.data.remote

import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow

class FakeRemoteData : RemoteDataSource {
    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProducts(): Flow<ProductResponse?> {
        TODO("Not yet implemented")
    }
}