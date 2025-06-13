package com.example.winkcart_user.data.repository


import com.example.winkcart_user.data.model.customer.Customer
import com.example.winkcart_user.data.model.customer.CustomerRequest
import com.example.winkcart_user.data.model.customer.CustomerResponse
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.orders.OrderDetailsResponse
import com.example.winkcart_user.data.model.orders.OrderRequest
import com.example.winkcart_user.data.model.orders.OrdersResponse
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse

import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.settings.address.CustomerAddress
import com.example.winkcart_user.data.model.settings.address.CustomerAddressRequest
import com.example.winkcart_user.data.model.settings.address.CustomerAddressesResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.settings.enums.Currency
import kotlinx.coroutines.flow.Flow

interface ProductRepo {

    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getLatestRateFromUSDToEGP(): Flow<CurrencyResponse?>

    suspend fun readCurrencyCode(): Flow<String>
    suspend fun writeCurrencyCode(currencyCode: Currency)

    suspend fun readCurrencyRate(): Flow<String>
    suspend fun writeCurrencyRate(currencyRate: String)

    suspend fun readCurrencyReadingDate(): Flow<String>
    suspend fun writeCurrencyReadingDate(currencyReadingDate: String)

    suspend fun readCustomerID(): Flow<String>
    suspend fun writeCustomerID(customerID: String)
     fun readCustomersID(): String

    suspend fun getFilteredSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getAllProducts() :  Flow<ProductResponse?>

    suspend fun getProductsByVendor(vendor : String) : Flow<ProductResponse?>

     fun getRate() : Float
     fun getReview(): String


    fun postCustomer (customer: CustomerRequest): Flow<CustomerResponse?>

    suspend fun createDraftOrder(
        draftOrderRequest: DraftOrderRequest
    ): Flow<Any>

    suspend fun getAllDraftOrders() : Flow<DraftOrderResponse?>
    suspend fun deleteDraftOrder(draftOrderId: Long): Flow<Unit?>

    suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrderRequest: DraftOrderRequest
    ): Flow<DraftOrderResponse?>

    suspend fun getPriceRules() : Flow<PriceRulesResponse?>
    suspend fun getUserOrders() : Flow<OrdersResponse?>
    suspend fun getSpecificOrderDetails(orderId: Long) : Flow<OrderDetailsResponse?>
    suspend fun createOrder(orderRequest: OrderRequest): Flow<OrdersResponse?>


    suspend fun addCustomerAddress(
        customerId: Long,
        customerAddressRequest: CustomerAddressRequest
    ): Flow<Any>

    suspend fun getCustomerAddresses(customerId: Long): Flow<CustomerAddressesResponse?>

    suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): Flow<Unit?>

    suspend fun getCustomerAddress(customerId: Long, addressId: Long): Flow<CustomerAddressRequest?>

    suspend fun updateCustomerAddress(
        customerId: Long,
        addressId: Long,
        customerAddressRequest: CustomerAddressRequest
    ): Flow<Any?>

}