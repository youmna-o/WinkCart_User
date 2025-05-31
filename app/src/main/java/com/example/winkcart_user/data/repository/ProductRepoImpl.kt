package com.example.winkcart_user.data.repository
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.map

import com.example.winkcart_user.data.model.products.ProductResponse
import kotlinx.coroutines.flow.Flow


class ProductRepoImpl ( private  val remoteDataSource: RemoteDataSource) : ProductRepo {


    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        return  remoteDataSource.getSmartCollections()

    }

    override suspend fun getFilteredSmartCollections(): Flow<SmartCollectionsResponse?> {
        return remoteDataSource.getSmartCollections()
            .map { response ->
                response?.let {
                    val filteredBrands = it.smart_collections
                        .filter { collection -> collection.image?.src != null }
                        .distinctBy { collection -> collection.title }
                    SmartCollectionsResponse(smart_collections = filteredBrands)
                }
            }
    }

    override suspend fun getAllProducts(): Flow<ProductResponse?> {
        return remoteDataSource.getAllProducts()
    }

    override suspend fun getProductsByVendor(vendor: String): Flow<ProductResponse?> {
        return remoteDataSource.getProductsByVendor(vendor)
            .map { response ->
                response?.let {
                    val filteredVendorPeoducts = it.products
                        .distinctBy { product -> product.title }
                    ProductResponse(products = filteredVendorPeoducts)
                }
           }
    }

    override suspend fun getRate(): Double{
        return  remoteDataSource.getRate()
    }

    override suspend fun getReview(): String {
        return remoteDataSource.getReview()
    }
}