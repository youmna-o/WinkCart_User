package com.example.winkcart_user.ui.home.vendorProducts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class VendorProductsViewModel(private  val repo: ProductRepo) : ViewModel(){

    private val _productsByVendor = MutableStateFlow<ResponseStatus<ProductResponse>>(ResponseStatus.Loading)
    val productByVendor = _productsByVendor.asStateFlow()

    fun getProductsPyVendor (vendor : String){
        viewModelScope.launch {
            repo.getProductsByVendor(vendor)
                .catch { exeption ->
                    _productsByVendor.value = ResponseStatus.Error(exeption)
                }.collect{ response ->
                    if(response!= null){
                        _productsByVendor.value = ResponseStatus.Success(response)
                    }else{
                        _productsByVendor.value = ResponseStatus.Error(
                            NullPointerException("SmartCollectionsResponse is null")
                        )
                    }
                }
        }
    }
}