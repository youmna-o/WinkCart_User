package com.example.winkcart_user.data.remote

import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
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




}