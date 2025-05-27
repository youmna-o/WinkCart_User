package com.example.winkcart_user.brands.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.data.ResponseStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class BrandsViewModel(private  val repo: ProductRepo) : ViewModel() {

    init {
       getSmartCollections()
    }

    private val _brandList = MutableStateFlow<ResponseStatus<SmartCollectionsResponse>>(ResponseStatus.Loading)
    val brandList = _brandList.asStateFlow()

    fun getSmartCollections() {
        viewModelScope.launch {
            val brands = repo.getSmartCollections()
            brands.catch {
                _brandList.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null){
                    _brandList.value= ResponseStatus.Success<SmartCollectionsResponse>(it)
                }else{
                    _brandList.value = ResponseStatus.Error(NullPointerException("SmartCollectionsResponse is null"))
                }
            }
        }
    }


}