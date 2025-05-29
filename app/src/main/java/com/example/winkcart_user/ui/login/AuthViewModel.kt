package com.example.winkcart_user.ui.login

import androidx.lifecycle.ViewModel
import com.example.winkcart_user.data.repository.FirebaseRepo
import java.util.regex.Matcher
import java.util.regex.Pattern


class AuthViewModel( private val repo: FirebaseRepo) : ViewModel(){
    fun signUp(email : String,password: String){
        repo.signUpFireBase(email,password)
    }
    fun signIn(email : String,password: String){
        repo.signInFireBase(email,password)
    }
    fun isEmailValid(email: String?): Boolean {
        var isValid = false
        val expression = "^[a-zA-Z0-9+_.-]+@[a-z-]+\\.[a-z]+"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

}
