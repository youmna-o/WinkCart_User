package com.example.winkcart_user.data.repository

import com.google.firebase.auth.FirebaseUser

interface FirebaseRepo {
    fun signUpFireBase(email: String,password: String) : FirebaseUser?
    fun signInFireBase(email: String,password: String) : FirebaseUser?
}