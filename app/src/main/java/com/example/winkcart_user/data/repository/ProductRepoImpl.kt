package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.local.LocalDataSource
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.map

import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.settings.enums.Currency
import kotlinx.coroutines.flow.Flow

class ProductRepoImpl ( private  val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource) : ProductRepo {

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
        .map { response ->
            response?.let {
                val filteredproducts = it.products
                    .distinctBy { product -> product.title }
                ProductResponse(products = filteredproducts)
            }
        }
    }

    override suspend fun getLatestRateFromUSDToEGP(): Flow<CurrencyResponse?> {
        return  remoteDataSource.getLatestRateFromUSDToEGP()
    }

    override suspend fun readCurrencyCode(): Flow<String> {
        return localDataSource.readCurrencyCode()
    }

    override suspend fun writeCurrencyCode(currencyCode: Currency) {
        localDataSource.writeCurrencyCode(currencyCode)
    }

    override suspend fun readCurrencyRate(): Flow<String> {
        return localDataSource.readCurrencyRate()
    }

    override suspend fun writeCurrencyRate(currencyRate: String) {
        return localDataSource.writeCurrencyRate(currencyRate)
    }

    override suspend fun readCurrencyReadingDate(): Flow<String> {
        return localDataSource.readCurrencyReadingDate()
    }

    override suspend fun writeCurrencyReadingDate(currencyReadingDate: String) {
        localDataSource.writeCurrencyReadingDate(currencyReadingDate)
    }

    override suspend fun readCustomerID(): Flow<String> {
        return localDataSource.readCustomerID()
    }

    override suspend fun writeCustomerID(customerID: String) {
        localDataSource.writeCustomerID(customerID)
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


    override  fun getRate(): Float {
        return  remoteDataSource.getRate()
    }

    override  fun getReview(): String {
        return remoteDataSource.getReview()
    }

    override suspend fun createDraftOrder(
        draftOrderRequest: DraftOrderRequest
    ): Flow<Any> {
        return remoteDataSource.createDraftOrder(draftOrderRequest)
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrderResponse?> {
        return  remoteDataSource.getAllDraftOrders()
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long): Flow<Unit?> {
        return remoteDataSource.deleteDraftOrder(draftOrderId = draftOrderId)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrderRequest: DraftOrderRequest
    ): Flow<DraftOrderResponse?> {
        return remoteDataSource.updateDraftOrder(
            draftOrderId = draftOrderId,
            draftOrderRequest = draftOrderRequest
        )
    }

    override suspend fun getPriceRules(): Flow<PriceRulesResponse?> {
        return  remoteDataSource.getPriceRules()
    }

}