package com.example.winkcart_user.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SettingsViewModel(private  val repo: ProductRepo) : ViewModel() {

    init {
        getLatestRateFromUSDToEGP()
    }

    private val _latestRateFromUSDToEGP = MutableStateFlow<ResponseStatus<CurrencyResponse>>(
        ResponseStatus.Loading)
    val latestRateFromUSDToEGP = _latestRateFromUSDToEGP.asStateFlow()

    private fun getLatestRateFromUSDToEGP() {
        viewModelScope.launch {
            val brands = repo.getLatestRateFromUSDToEGP()
            brands.catch {
                _latestRateFromUSDToEGP.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null){
                    _latestRateFromUSDToEGP.value= ResponseStatus.Success<CurrencyResponse>(it)
                }else{
                    _latestRateFromUSDToEGP.value = ResponseStatus.Error(NullPointerException("CurrencyResponse is null"))
                }
            }
        }
    }
}