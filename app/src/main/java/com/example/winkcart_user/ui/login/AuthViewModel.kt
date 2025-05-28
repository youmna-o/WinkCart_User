package com.example.winkcart_user.ui.login

import androidx.lifecycle.ViewModel
import com.example.winkcart_user.data.repository.FirebaseRepo

class AuthViewModel( private val repo: FirebaseRepo) : ViewModel(){
    fun signUp(email : String,password: String){
        repo.signUpFireBase(email,password)
    }
    fun signIn(email : String,password: String){
        repo.signInFireBase(email,password)
    }

}