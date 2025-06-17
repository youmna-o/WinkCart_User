package com.example.winkcart_user.ui.profile.userProfile.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.winkcart_user.data.repository.FirebaseRepo
import com.example.winkcart_user.data.repository.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor( private val repo: FirebaseRepo,  val productRepo: ProductRepo) : ViewModel(){
    
    fun getGemail(): String{
        return repo.getUserGemail()
    }
    fun isGuest(): Boolean {
        return productRepo.readCustomersID().isBlank()
    }
}

class ProfileFactory(private  val  repo: FirebaseRepo,private val productRepo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            repo,
            productRepo = productRepo
        ) as T
    }
}