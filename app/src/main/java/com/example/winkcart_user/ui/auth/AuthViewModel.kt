package com.example.winkcart_user.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.winkcart_user.data.repository.FirebaseRepo
import java.util.regex.Pattern


class AuthViewModel( private val repo: FirebaseRepo) : ViewModel(){

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


class AuthFactory(private  val  repo: FirebaseRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repo) as T
    }
}

