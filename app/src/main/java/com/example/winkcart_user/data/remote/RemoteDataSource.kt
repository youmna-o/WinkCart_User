package com.example.winkcart_user.data.remote

import com.example.winkcart_user.data.model.coupons.discount.DiscountCodesResponse
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
import com.example.winkcart_user.data.model.settings.address.CustomerAddressRequest
import com.example.winkcart_user.data.model.settings.address.CustomerAddressesResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse?>
    suspend fun getLatestRateFromUSDToEGP() : Flow<CurrencyResponse?>

    suspend fun getAllProducts (): Flow<ProductResponse?>

    suspend fun getProductsByVendor(vendor : String): Flow<ProductResponse?>


     fun getRate (): Float

    fun getReview (): String

    fun signUpFireBase(email: String,password: String) :Task<AuthResult>

    fun signInFireBase(email: String,password: String) : Task<AuthResult>

    fun postCustomer (customer: CustomerRequest): Flow<CustomerResponse?>

    fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult>
    fun signOutFireBase()

        suspend fun createDraftOrder(
        draftOrderRequest: DraftOrderRequest
    ): Flow<Any>


    suspend fun getAllDraftOrders(): Flow<DraftOrderResponse?>
    suspend fun deleteDraftOrder(draftOrderId: Long): Flow<Unit?>

    suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrderRequest: DraftOrderRequest
    ): Flow<DraftOrderResponse?>

    suspend fun getPriceRules(): Flow<PriceRulesResponse?>
    suspend fun getOrders(customerId:Long) :Flow<OrdersResponse?>
    suspend fun  getSpecificOrderDEtails(orderId:Long):Flow<OrderDetailsResponse?>
    suspend fun createOrder(orderRequest: OrderRequest):Flow<OrdersResponse?>

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

    suspend fun deleteCustomerAddress(customerId: Long, addressId: Long): Flow<Unit?>

    suspend fun getDiscountCodesByPriceRule(priceRuleId: Long): Flow<DiscountCodesResponse?>

    //map
    fun getPlacesApiAutoComplete(
        query: String,
        placesClient: PlacesClient
    ): Task<FindAutocompletePredictionsResponse>

    fun fetchPlaceById(placeId: String, placesClient: PlacesClient): Task<FetchPlaceResponse>



}