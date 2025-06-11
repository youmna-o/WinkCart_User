package com.example.winkcart_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel(private val repo: ProductRepo) :ViewModel() {
    private val _currencyCode = MutableStateFlow("")
    val currencyCode = _currencyCode.asStateFlow()

    private val _currencyRate = MutableStateFlow("")
    val currencyRate = _currencyRate.asStateFlow()

    init {
        readCurrencyCode()
        readCurrencyRate()
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
class CurrencyFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrencyViewModel(repo) as T
    }
}