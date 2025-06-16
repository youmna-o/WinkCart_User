package com.example.winkcart_user.data.source

import com.example.winkcart_user.data.model.coupons.discount.DiscountCodesResponse
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.customer.CustomerRequest
import com.example.winkcart_user.data.model.customer.CustomerResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.model.orders.OrderDetailsResponse
import com.example.winkcart_user.data.model.orders.OrderRequest
import com.example.winkcart_user.data.model.orders.OrdersResponse
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.settings.address.CustomerAddressRequest
import com.example.winkcart_user.data.model.settings.address.CustomerAddressesResponse
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.remote.RemoteDataSource
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

class FakeRemoteDataSource : RemoteDataSource {
    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestRateFromUSDToEGP(): Flow<CurrencyResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProducts(): Flow<ProductResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsByVendor(vendor: String): Flow<ProductResponse?> {
        TODO("Not yet implemented")
    }

    override fun getRate(): Float {
        TODO("Not yet implemented")
    }

    override fun getReview(): String {
        TODO("Not yet implemented")
    }

    override fun signUpFireBase(
        email: String,
        password: String
    ): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun signInFireBase(
        email: String,
        password: String
    ): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun postCustomer(customer: CustomerRequest): Flow<CustomerResponse?> {
        TODO("Not yet implemented")
    }

    override fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun signOutFireBase() {
        TODO("Not yet implemented")
    }

    override suspend fun createDraftOrder(draftOrderRequest: DraftOrderRequest): Flow<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrderResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long): Flow<Unit?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrderRequest: DraftOrderRequest
    ): Flow<DraftOrderResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getPriceRules(): Flow<PriceRulesResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(customerId: Long): Flow<OrdersResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpecificOrderDEtails(orderId: Long): Flow<OrderDetailsResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun createOrder(orderRequest: OrderRequest): Flow<OrdersResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun addCustomerAddress(
        customerId: Long,
        customerAddressRequest: CustomerAddressRequest
    ): Flow<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerAddresses(customerId: Long): Flow<CustomerAddressesResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): Flow<Unit?> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerAddress(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddressRequest?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCustomerAddress(
        customerId: Long,
        addressId: Long,
        customerAddressRequest: CustomerAddressRequest
    ): Flow<Any?> {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscountCodesByPriceRule(priceRuleId: Long): Flow<DiscountCodesResponse?> {
        TODO("Not yet implemented")
    }

    override fun getPlacesApiAutoComplete(
        query: String,
        placesClient: PlacesClient
    ): Task<FindAutocompletePredictionsResponse> {
        TODO("Not yet implemented")
    }

    override fun fetchPlaceById(
        placeId: String,
        placesClient: PlacesClient
    ): Task<FetchPlaceResponse> {
        TODO("Not yet implemented")
    }
}