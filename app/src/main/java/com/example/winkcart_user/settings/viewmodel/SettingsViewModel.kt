package com.example.winkcart_user.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.settings.address.CustomerAddress
import com.example.winkcart_user.data.model.settings.address.CustomerAddressRequest
import com.example.winkcart_user.data.model.settings.address.CustomerAddressesResponse
import com.example.winkcart_user.data.model.settings.currency.CurrencyResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.settings.enums.Currency
import com.example.winkcart_user.settings.model.AddressFormValidationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel@Inject constructor(private  val repo: ProductRepo) : ViewModel() {

    private val _latestRateFromUSDToEGP = MutableStateFlow<ResponseStatus<CurrencyResponse>>(
        ResponseStatus.Loading)

    private val _currencyCode = MutableStateFlow("")
    val currencyCode = _currencyCode.asStateFlow()

    private val _addCustomerAddressResponse = MutableStateFlow<ResponseStatus<Any>>(ResponseStatus.Loading)
   // val addCustomerAddressResponse = _addCustomerAddressResponse.asStateFlow()

    private val _customerID = MutableStateFlow("")
    val customerID = _customerID.asStateFlow()

    private val _formValidationState = MutableStateFlow(AddressFormValidationState())
    val formValidationState = _formValidationState.asStateFlow()

    private val _customerAddresses = MutableStateFlow<ResponseStatus<CustomerAddressesResponse>>(ResponseStatus.Loading)
    val customerAddresses = _customerAddresses.asStateFlow()

    private val _deleteCustomerAddresses = MutableStateFlow<ResponseStatus<Unit>>(ResponseStatus.Loading)
    val deleteCustomerAddresses = _deleteCustomerAddresses.asStateFlow()

    private val _defaultCustomerAddresses = MutableStateFlow<ResponseStatus<CustomerAddress>>(ResponseStatus.Loading)
    val defaultCustomerAddresses = _defaultCustomerAddresses.asStateFlow()

    private val _customerDefaultAddressResponse = MutableStateFlow<ResponseStatus<Unit>>(ResponseStatus.Loading)
    //val customerDefaultAddressResponse = _customerDefaultAddressResponse.asStateFlow()

    private val _defaultCity = MutableStateFlow<ResponseStatus<String>>(ResponseStatus.Loading)
    val defaultCity = _defaultCity.asStateFlow()

    private val _customerAddress = MutableStateFlow<ResponseStatus<CustomerAddressRequest>>(ResponseStatus.Loading)
    val customerAddress = _customerAddress.asStateFlow()

    private val _customerAddressUpdateResponse = MutableStateFlow<ResponseStatus<Any>>(ResponseStatus.Loading)
    val customerAddressUpdateResponse = _customerAddressUpdateResponse.asStateFlow()

    init {
        readCustomerID()
       // Log.i("TAG", " Setting inti CustomerID: ${customerID.value} ")
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


    fun validateAddressForm(
        title: String,
        /*selectedGovernorate: String,*/
        address: String,
        contactPerson: String,
        phoneNumber: String
    ): Boolean {
        val validEgyptianPrefixes = listOf("10", "11", "12", "15")
        val isValidPhone = phoneNumber.length == 10 && validEgyptianPrefixes.any { phoneNumber.startsWith(it) }

        val validation = AddressFormValidationState(
            titleError = title.isBlank(),
            /*governorateError = selectedGovernorate.isBlank(),*/
            addressError = address.isBlank(),
            contactPersonError = contactPerson.isBlank(),
            phoneError = !isValidPhone
        )

        _formValidationState.value = validation

        return with(validation) {
            !titleError && !governorateError && !addressError && !contactPersonError && !phoneError
        }
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

    fun addCustomerAddress(customerId: Long, customerAddressRequest: CustomerAddressRequest) {
        viewModelScope.launch {
            val result =
                repo.addCustomerAddress(
                    customerId = customerId,
                    customerAddressRequest = customerAddressRequest
                )
            result.catch {
                _addCustomerAddressResponse.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null){
                    _addCustomerAddressResponse.value= ResponseStatus.Success<Any>(it)
                    getCustomerAddresses(customerId)

                }else{
                    _addCustomerAddressResponse.value = ResponseStatus.Error(NullPointerException("customerAddressRequest is null"))
                }
            }

        }
    }

     fun readCustomerID(){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.readCustomerID()
            result.collect{
                _customerID.value = it
            }
        }
    }

    fun getCustomerAddresses(customerId: Long) {
        viewModelScope.launch {
            repo.getCustomerAddresses(customerId)
                .catch { exception ->
                    _customerAddresses.value = ResponseStatus.Error(exception)
                }.collect{ response ->
                    if(response!= null){
                        _customerAddresses.value = ResponseStatus.Success(response)
                        val default = response.addresses.find { it.default }

                        if (default != null) {
                            _defaultCustomerAddresses.value = ResponseStatus.Success(default)
                            _defaultCity.value = ResponseStatus.Success(default.city)
                        } else {
                            _defaultCustomerAddresses.value = ResponseStatus.Error(NullPointerException("No default address found"))
                            _defaultCity.value = ResponseStatus.Error(NullPointerException("No default address found"))
                        }


                    }else{
                        _customerAddresses.value = ResponseStatus.Error(
                            NullPointerException("Customer Addresses is null")
                        )
                    }
                }
        }
    }

    fun setDefaultAddress(customerId: Long, addressId: Long) {
        viewModelScope.launch {
            repo.setDefaultAddress(
                customerId = customerId,
                addressId = addressId
            )
                .catch { exception ->
                    _customerDefaultAddressResponse.value = ResponseStatus.Error(exception)
                }.collect{ response ->
                    if(response!= null){
                        _customerDefaultAddressResponse.value = ResponseStatus.Success(response)
                        getCustomerAddresses(customerId)
                    }else{
                        _customerDefaultAddressResponse.value = ResponseStatus.Error(
                            NullPointerException("Customer default Address is null")
                        )
                    }
                }
        }
    }

    fun getCustomerAddress(customerId: Long, addressId: Long) {
        viewModelScope.launch {
            repo.getCustomerAddress(customerId, addressId)
                .catch { exception ->
                    _customerAddress.value = ResponseStatus.Error(exception)
                }.collect{ response ->
                    if(response!= null){
                        _customerAddress.value = ResponseStatus.Success(response)


                    }else{
                        _customerAddress.value = ResponseStatus.Error(
                            NullPointerException("Customer Address is null")
                        )
                    }
                }
        }
    }

    fun updateCustomerAddress(customerId: Long, addressId: Long, customerAddressRequest: CustomerAddressRequest) {
        viewModelScope.launch {
            repo.updateCustomerAddress(customerId, addressId, customerAddressRequest)
                .catch { exception ->
                    _customerAddressUpdateResponse.value = ResponseStatus.Error(exception)
                }.collect{ response ->
                    if(response!= null){
                        _customerAddressUpdateResponse.value = ResponseStatus.Success(response)
                        getCustomerAddresses(customerId)

                    }else{
                        _customerAddressUpdateResponse.value = ResponseStatus.Error(
                            NullPointerException("Customer Address update response is null")
                        )
                    }
                }
        }
    }


    fun deleteCustomerAddress(customerId: Long, addressId: Long) {
        viewModelScope.launch {
            repo.deleteCustomerAddress(customerId, addressId)
                .catch { exception ->
                    _deleteCustomerAddresses.value = ResponseStatus.Error(exception)
                }.collect{ response ->
                    if(response!= null){
                        _deleteCustomerAddresses.value = ResponseStatus.Success(response)
                        getCustomerAddresses(customerId)

                    }else{
                        _deleteCustomerAddresses.value = ResponseStatus.Error(
                            NullPointerException("Customer Address is not deleted")
                        )
                    }
                }
        }
    }
}

class SettingsFactory(private val repo: ProductRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return SettingsViewModel(repo) as T
    }
}