package com.example.winkcart_user.ui.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.customer.CustomerRequest

import com.example.winkcart_user.data.repository.FirebaseRepo
import com.example.winkcart_user.data.repository.ProductRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern


class AuthViewModel( private val repo: FirebaseRepo, private val customerRepo : ProductRepoImpl) : ViewModel(){

    var emailError = mutableStateOf<String?>(null)
    var passwordError = mutableStateOf<String?>(null)

    fun signUp(email: String, password: String, onResult: (Boolean) -> Unit) {
        if (Uservalidate(email, password)) {
            repo.signUpFireBase(email, password)
                .addOnCompleteListener { task ->
                    onResult(task.isSuccessful)
                }
        } else {
            onResult(false)
        }
    }

    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        if (Uservalidate(email, password)) {
            repo.signInFireBase(email, password)
                .addOnCompleteListener { task ->
                    onResult(task.isSuccessful)
                }
        } else {
            onResult(false)
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



    private fun Uservalidate(email: String, password: String): Boolean {
        var isValid = true
        if (email.isBlank()) {
            emailError.value = "Email is required"
            isValid = false
        } else if (!isEmailValid(email)) {
            emailError.value = "Invalid email format"
            isValid = false
        } else {
            emailError.value = null
        }

        if (password.isBlank()) {
            passwordError.value = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordError.value = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordError.value = null
        }

        return isValid
    }

    fun isEmailValid(email: String): Boolean {
        val expression = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return Pattern.compile(expression).matcher(email).matches()
    }





}


class AuthFactory(private  val  repo: FirebaseRepo, private val customerRepo : ProductRepoImpl): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repo, customerRepo) as T
    }
}

