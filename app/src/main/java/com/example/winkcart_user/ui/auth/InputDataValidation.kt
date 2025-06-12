//package com.example.winkcart_user.ui.auth
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.unit.Constraints
//import com.example.winkcart_user.utils.Constants
//import java.util.regex.Pattern
//
//object InputDataValidation {
//    var emailError = mutableStateOf<String?>(null)
//    var passwordError = mutableStateOf<String?>(null)
//    fun isValidInputsData(email: String, password: String): Boolean {
//        var isValid = true
//        if (email.isBlank()) {
//            emailError.value = "Email is required"
//            isValid = false
//        } else if (!isEmailValid(email)) {
//            emailError.value = "Invalid email format"
//            isValid = false
//        } else {
//            emailError.value = null
//        }
//
//        if (password.isBlank()) {
//            passwordError.value = "Password is required"
//            isValid = false
//        } else if (password.length < 6) {
//            passwordError.value = "Password must be at least 6 characters"
//            isValid = false
//        } else if (!isPasswordValid(password)) {
//            passwordError.value = "Password must contain uppercase, lowercase, digit, and symbol"
//            isValid = false
//        } else {
//            passwordError.value = null
//        }
//
//
//        return isValid
//    }
//
//    fun isEmailValid(email: String): Boolean {
//        val expression = Constants.Email_Regix
//        return Pattern.compile(expression).matcher(email).matches()
//    }
//    fun isPasswordValid(password: String): Boolean {
//        val passwordRegex = Constants.Password_Regix
//        return Pattern.compile(passwordRegex).matcher(password).matches()
//    }
//}