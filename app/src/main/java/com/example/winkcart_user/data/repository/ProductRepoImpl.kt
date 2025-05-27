package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.local.LocalDataSource
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductRepoImpl ( private  val remoteDataSource: RemoteDataSource) : ProductRepo {


    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        return  remoteDataSource.getSmartCollections()
    }

    override suspend fun getAllProducts(): Flow<ProductResponse?> {
        return remoteDataSource.getAllProducts()
    }

}