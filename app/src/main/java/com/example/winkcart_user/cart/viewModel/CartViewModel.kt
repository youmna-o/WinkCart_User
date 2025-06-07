package com.example.winkcart_user.cart.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderResponse
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CartViewModel (private val repo: ProductRepo ) :ViewModel() {


    private val _customerID = MutableStateFlow("")
    val customerID = _customerID.asStateFlow()

    private val _createDraftOrderResponse = MutableStateFlow<ResponseStatus<Any>>(ResponseStatus.Loading)
    val createDraftOrderResponse = _createDraftOrderResponse.asStateFlow()

    private val _draftOrders = MutableStateFlow<ResponseStatus<DraftOrderResponse>>(ResponseStatus.Loading)
    val draftOrders = _draftOrders.asStateFlow()

    init {
        viewModelScope.launch {
            repo.readCustomerID().collect { id ->
                _customerID.value = id
                getDraftOrders(id)
            }
        }
    }





    fun createDraftOrder(draftOrderRequest: DraftOrderRequest) {
        viewModelScope.launch {
            val result = repo.createDraftOrder(draftOrderRequest)
            result.catch {
                _createDraftOrderResponse.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null){
                    _createDraftOrderResponse.value= ResponseStatus.Success<Any>(it)
                }else{
                    _createDraftOrderResponse.value = ResponseStatus.Error(NullPointerException("createDraftOrderResponse is null"))
                }
            }

        }
    }

  /*  private fun getDraftOrders(customerId: String) {
        viewModelScope.launch {
            val result = repo.getAllDraftOrders()
            result
                .catch {
                    _draftOrders.value = ResponseStatus.Error(it)
                }
                .collect { response ->
                    if (response != null) {
                        val filtered = response.draft_orders.filter {
                            it.customer.id.toString() == customerId
                        }
                        _draftOrders.value = ResponseStatus.Success(
                            DraftOrderResponse(draft_orders = filtered)
                        )
                    } else {
                        _draftOrders.value = ResponseStatus.Error(
                            NullPointerException("DraftOrders is null")
                        )
                    }
                }
        }
    }*/
    private fun getDraftOrders(customerId: String) {
        viewModelScope.launch {
            val result = repo.getAllDraftOrders()
            result
                .catch {
                    _draftOrders.value = ResponseStatus.Error(it)
                }
                .collect { response ->
                    if (response != null) {

                        _draftOrders.value = ResponseStatus.Success(
                            DraftOrderResponse(draft_orders = response.draft_orders)

                        )
                        Log.i("TAG", "getDraftOrders: ${response.draft_orders[0].customer.id} ")
                    } else {
                        _draftOrders.value = ResponseStatus.Error(
                            NullPointerException("DraftOrders is null")
                        )
                    }
                }
        }
    }


    private fun readCustomerID(){
        viewModelScope.launch (Dispatchers.IO) {
            val result = repo.readCustomerID()
            result.collect{
                _customerID.value = it
            }
        }
    }



}
class CartFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(repo) as T
    }
}
