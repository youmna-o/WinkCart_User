package com.example.winkcart_user.profile.userProfile.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.repository.FirebaseRepo
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun writeCustomerID(id: String?){
        viewModelScope.launch (Dispatchers.IO) {
            val result = productRepo.writeCustomerID(id.toString())
        }
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