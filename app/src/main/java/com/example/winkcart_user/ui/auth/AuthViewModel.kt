package com.example.winkcart_user.ui.auth

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.model.customer.CustomerRequest
import com.example.winkcart_user.data.repository.FirebaseRepo
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.utils.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


import java.util.regex.Pattern


class AuthViewModel( private val repo: FirebaseRepo, private val customerRepo : ProductRepoImpl) : ViewModel(){


    private val _emailError = MutableStateFlow("")
    val emailError = _emailError.asStateFlow()
    private val _passwordError  = MutableStateFlow("")
    val passwordError  = _passwordError .asStateFlow()

     fun signUp(
         email: String,
         password: String,
         onVerificationSent: (Boolean) -> Unit,
         onVerified: (FirebaseUser?) -> Unit
     ) {
         if (isValidInputsData(email, password)) {
        repo.signUpFireBase(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    repo.sendEmailVerification { verificationSent ->
                        if (verificationSent) {
                                Log.i("email", "signUp: success link email")
                            onVerificationSent(true)
                            holdEmailVerification(email) { verifiedUser ->
                                if (verifiedUser != null) {
                                    onVerified(verifiedUser)
                                }
                            }
                        } else {
                                Log.i("email", "signUp: failed link email")
                            onVerificationSent(false)
                        }
                    }
                } else {
                    onVerificationSent(false)
                }
            }
    } else {
        onVerificationSent(false)
    }
}

    private fun holdEmailVerification(email: String, onVerified: (FirebaseUser?) -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        val checkVerificationRunnable = object : Runnable {
            override fun run() {
                val user = Firebase.auth.currentUser
                user?.reload()?.addOnCompleteListener { reloadTask ->
                    if (reloadTask.isSuccessful && user.isEmailVerified) {
                        onVerified(user)
                    } else {
                        handler.postDelayed(this, 50)
                    }
                }
            }
        }
        handler.post(checkVerificationRunnable)
    }
    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        if (isValidInputsData(email, password)) {
            repo.signInFireBase(email, password)
                .addOnCompleteListener { task ->
                    onResult(task.isSuccessful)
                }
        } else {
            onResult(false)
        }
    }
    fun signInWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        repo.firebaseAuthWithGoogle(idToken)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }


    fun postCustomer(
        customerRequest: CustomerRequest,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {
            customerRepo.postCustomer(customerRequest)
                .catch { e ->
                    onResult(null)
                }
                .collect { response ->
                    val customerId = response?.customer?.id?.toString()
                    Log.i("shared", "From postCustomer:${response?.customer?.id?.toString()} ")
                    onResult(customerId)
                }
        }
    }



    private fun isValidInputsData(email: String, password: String): Boolean {
        var isValid = true
        if (email.isBlank()) {
            _emailError.value = "Email is required"
            isValid = false
        } else if (!isEmailValid(email)) {
            _emailError.value  = "Invalid email format"
            isValid = false
        } else {
            _emailError.value  = ""
        }

        if (password.isBlank()) {
            _passwordError.value = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            _passwordError.value = "Password must be at least 6 characters"
            isValid = false
        } else if (!isPasswordValid(password)) {
            _passwordError.value = "Password must contain uppercase, lowercase, digit, and symbol"
            isValid = false
        } else {
            _passwordError.value = ""
        }
        return isValid
    }

    fun isEmailValid(email: String): Boolean {
        val expression = Constants.Email_Regix
        return Pattern.compile(expression).matcher(email).matches()
    }
    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Constants.Email_Regix
        return Pattern.compile(passwordRegex).matcher(password).matches()
    }






}


class AuthFactory(private  val  repo: FirebaseRepo, private val customerRepo : ProductRepoImpl): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repo, customerRepo) as T
    }
}

