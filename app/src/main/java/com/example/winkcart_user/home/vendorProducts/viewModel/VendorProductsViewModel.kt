package com.example.winkcart_user.home.vendorProducts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.repository.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorProductsViewModel@Inject constructor(private  val repo: ProductRepo) : ViewModel(){

    private val _productsByVendor = MutableStateFlow<ResponseStatus<ProductResponse>>(ResponseStatus.Loading)
    val productByVendor = _productsByVendor.asStateFlow()

    private val _currencyCode = MutableStateFlow("")
    val currencyCode = _currencyCode.asStateFlow()

    private val _currencyRate = MutableStateFlow("")
    val currencyRate = _currencyRate.asStateFlow()


    private val _searchInput = MutableStateFlow("")
    val searchInput = _searchInput.asStateFlow()

    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())

    init {
        readCurrencyCode()
        readCurrencyRate()
    }
    val filteredProducts = combine(_filteredProducts, _searchInput) { products, search ->
        if (search.isBlank()) products
        else products.filter { it.title.contains(search, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onSearchInputChanged(input: String) {
        _searchInput.value = input
    }


    fun getProductsPyVendor (vendor : String){
        viewModelScope.launch {
            repo.getProductsByVendor(vendor)
                .catch { exeption ->
                    _productsByVendor.value = ResponseStatus.Error(exeption)
                }.collect{ response ->
                    if(response!= null){
                        _productsByVendor.value = ResponseStatus.Success(response)
                        _filteredProducts.value = response.products
                    }else{
                        _productsByVendor.value = ResponseStatus.Error(
                            NullPointerException("SmartCollectionsResponse is null")
                        )
                    }
                }
        }
    }

    fun readCurrencyCode(){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.readCurrencyCode()
            result.collect{
                _currencyCode.value = it
            }
        }
    }

    fun readCurrencyRate(){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.readCurrencyRate()
            result.collect{
                _currencyRate.value = it
            }
        }
    }
}
class VendorsProductFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VendorProductsViewModel(repo) as T
    }
}
