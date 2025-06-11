package com.example.winkcart_user.settings.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.settings.enums.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class SettingsViewModel(private  val repo: ProductRepo) : ViewModel() {

    private val _latestRateFromUSDToEGP = MutableStateFlow<ResponseStatus<CurrencyResponse>>(
        ResponseStatus.Loading)

    private val _currencyCode = MutableStateFlow("")
    val currencyCode = _currencyCode.asStateFlow()

    init {
        viewModelScope.launch {
            val storedDate = try {
                repo.readCurrencyReadingDate().first()
            } catch (e: NoSuchElementException) {
                ""
            }

            val today = LocalDate.now().toString()

            if (storedDate != today) {
                getLatestRateFromUSDToEGP()
            }
        }
        readCurrencyCode()
    }



    private fun getLatestRateFromUSDToEGP() {
        viewModelScope.launch {
            val rateFlow = repo.getLatestRateFromUSDToEGP()
            rateFlow.catch {
                _latestRateFromUSDToEGP.value = ResponseStatus.Error(it)
            }.collect{ response ->
                if (response!= null){
                    _latestRateFromUSDToEGP.value = ResponseStatus.Success(response)

                    val rate = (response.data.EGP.value).toString()
                    writeCurrencyRate(rate)
                    writeCurrencyReadingDate(LocalDate.now().toString())
                }else{
                    _latestRateFromUSDToEGP.value = ResponseStatus.Error(NullPointerException("CurrencyResponse is null"))
                }
            }
        }
    }


    private fun readCurrencyCode(){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.readCurrencyCode()
            result.collect{
                _currencyCode.value = it
            }
        }
    }

    fun writeCurrencyCode(currencyCode:Currency){
        viewModelScope.launch (Dispatchers.IO) {
            _currencyCode.value = currencyCode.name
            repo.writeCurrencyCode(currencyCode)

        }
    }

    private fun writeCurrencyRate(currencyRate:String){
        viewModelScope.launch (Dispatchers.IO) {
            repo.writeCurrencyRate(currencyRate)

        }
    }

    private fun writeCurrencyReadingDate(currencyReadingDate:String){
        viewModelScope.launch (Dispatchers.IO) {
            repo.writeCurrencyReadingDate(currencyReadingDate)

        }
    }
}

class SettingsFactory(private val repo: ProductRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return SettingsViewModel(repo) as T
    }
}