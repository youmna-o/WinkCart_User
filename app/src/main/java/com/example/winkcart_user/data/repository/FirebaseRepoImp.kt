package com.example.winkcart_user.data.repository

import com.example.winkcart_user.data.remote.RemoteDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

class FirebaseRepoImp(private  val remoteDataSource: RemoteDataSource) : FirebaseRepo{
    override fun signUpFireBase(
        email: String,
        password: String
    ): Task<AuthResult> {
        return remoteDataSource.signUpFireBase(email,password)
    }

    override fun signInFireBase(
        email: String,
        password: String
    ): Task<AuthResult>{
        return remoteDataSource.signInFireBase(email,password)
    }
}