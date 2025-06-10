package com.example.winkcart_user.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.coupons.pricerule.PriceRulesResponse
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class FavouriteViewModel (private val repo: ProductRepo ) :ViewModel() {

    private val _currencyCode = MutableStateFlow("")
    val currencyCode = _currencyCode.asStateFlow()

    private val _currencyRate = MutableStateFlow("")
    val currencyRate = _currencyRate.asStateFlow()

    private val _customerID = MutableStateFlow("")
    val customerID = _customerID.asStateFlow()

    private val _draftOrders = MutableStateFlow<ResponseStatus<DraftOrderResponse>>(ResponseStatus.Loading)
    val draftOrders = _draftOrders.asStateFlow()

    private val _createDraftOrderResponse = MutableStateFlow<ResponseStatus<Any>>(ResponseStatus.Loading)
    private val _deleteDraftOrders = MutableStateFlow<ResponseStatus<Unit>>(ResponseStatus.Loading)
    //val deleteDraftOrders = _deleteDraftOrders.asStateFlow()


    private val _totalAmount = MutableStateFlow("0.00 EGP")
    val totalAmount = _totalAmount.asStateFlow()

    private val _priceRules = MutableStateFlow<ResponseStatus<PriceRulesResponse>>(ResponseStatus.Loading)
    val priceRules = _priceRules.asStateFlow()

    private val _draftProductTitles = MutableStateFlow<Set<String>>(emptySet())
    val draftProductTitles = _draftProductTitles.asStateFlow()


    init {
        readCurrencyCode()
        readCurrencyRate()
        viewModelScope.launch {
            repo.readCustomerID().collect { id ->
                _customerID.value = id
                getDraftOrders(id)
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


    fun getDraftOrders(customerId: String) {
        viewModelScope.launch {
            try {
                val storedCustomerIdStr = repo.readCustomersID()
                val storedCustomerId = storedCustomerIdStr?.toLongOrNull()

                if (storedCustomerId == null) {
                    _draftOrders.value = ResponseStatus.Error(
                        IllegalStateException("Invalid customer ID: $storedCustomerIdStr")
                    )
                    return@launch //stop running
                }

                repo.getAllDraftOrders()
                    .catch { exception ->
                        _draftOrders.value = ResponseStatus.Error(exception)
                    }
                    .collect { response ->
                        if (response != null) {
                            val matchingDraftOrders = response.draft_orders
                                .filter { it.customer.id == storedCustomerId }
                                .filter { draftOrder ->
                                    draftOrder.line_items.any { lineItem ->
                                        lineItem!!.properties.any { prop ->
                                            prop.name == "SavedAt" && prop.value == "Favourite"
                                        }
                                    }
                                }
                            val productTitles = matchingDraftOrders.flatMap { it.line_items.mapNotNull { item -> item?.title } }.toSet()
                            _draftProductTitles.value = productTitles

                            _draftOrders.value = ResponseStatus.Success(
                                response.copy(draft_orders = matchingDraftOrders)

                            )
                        } else {
                            _draftOrders.value = ResponseStatus.Error(
                                NullPointerException("Draft Orders is null")
                            )
                        }
                    }
            } catch (e: Exception) {
                _draftOrders.value = ResponseStatus.Error(e)
            }
        }
    }
    fun createDraftFavouriteOrder(customerId: String, draftOrderRequest: DraftOrderRequest) {
        viewModelScope.launch {
            val existingOrders = repo.getAllDraftOrders().firstOrNull()
            val isAlreadyExist = existingOrders?.draft_orders?.any { existingOrder ->
                existingOrder.customer?.id == customerId.toLong() &&
                        existingOrder.line_items.map { it?.title }.toSet() == draftOrderRequest.draft_order.line_items.map { it?.title }.toSet() &&
                        existingOrder.line_items.any { lineItem ->
                            lineItem?.properties?.any { prop ->
                                prop.name == "SavedAt" && prop.value == "Favourite"
                            } == true
                        }
            } == true

            if (!isAlreadyExist) {
                repo.createDraftOrder(draftOrderRequest)
                    .catch { /* handle error */ }
                    .collect {
                        // بعد الإضافة بنجاح، نعيد تحميل الطلبات
                        getDraftOrders(customerId)
                    }
            }
        }
    }


    fun deleteDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            repo.deleteDraftOrder(draftOrderId = draftOrderId)
                .catch { exception ->
                    _deleteDraftOrders.value = ResponseStatus.Error(exception)
                }.collect{ response ->
                    if(response!= null){
                        _deleteDraftOrders.value = ResponseStatus.Success(response)
                        getDraftOrders(_customerID.value)

                    }else{
                        _deleteDraftOrders.value = ResponseStatus.Error(
                            NullPointerException("Delete Draft Orders is null")
                        )
                    }
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
class FavouriteFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouriteViewModel(repo) as T
    }
}
