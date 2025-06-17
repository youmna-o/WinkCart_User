package com.example.winkcart_user.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface FirebaseRepo {
    fun signUpFireBase(email: String, password: String): Task<AuthResult>
    fun signInFireBase(email: String, password: String): Task<AuthResult>
    fun sendEmailVerification(onComplete: (Boolean) -> Unit)
    fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult>
    fun signOutFireBase()
    fun getUserGemail(): String


}
