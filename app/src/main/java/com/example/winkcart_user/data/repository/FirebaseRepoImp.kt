package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.remote.RemoteDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class FirebaseRepoImp(private  val remoteDataSource: RemoteDataSource) : FirebaseRepo {
    override fun signUpFireBase(
        email: String,
        password: String
    ): Task<AuthResult> {
        return remoteDataSource.signUpFireBase(email, password)
    }

    override fun signInFireBase(
        email: String,
        password: String
    ): Task<AuthResult> {
        return remoteDataSource.signInFireBase(email, password)
    }

    override fun sendEmailVerification(onComplete: (Boolean) -> Unit) {
        val user = Firebase.auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
            ?: onComplete(false)
    }

    override fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult> {
        return remoteDataSource.firebaseAuthWithGoogle(idToken)
    }


    override fun signOutFireBase() {
        return remoteDataSource.signOutFireBase()

    }
    override fun getUserGemail(): String {
        return Firebase.auth.currentUser?.email ?: ""

    }

}