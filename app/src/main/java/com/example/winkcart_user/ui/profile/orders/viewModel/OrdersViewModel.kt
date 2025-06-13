package com.example.winkcart_user.ui.profile.orders.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.orders.OrderDetailsResponse
import com.example.winkcart_user.data.model.orders.OrdersResponse
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: ProductRepo) : ViewModel() {
    private val _ordersList = MutableStateFlow<ResponseStatus<OrdersResponse>>(ResponseStatus.Loading)
    val ordersList = _ordersList.asStateFlow()

    private val _orderDetails = MutableStateFlow<ResponseStatus<OrderDetailsResponse>>(ResponseStatus.Loading)
    val orderDetails = _orderDetails.asStateFlow()

    init {
        getUserOrders()
    }

    fun getUserOrders(){
        viewModelScope.launch {
            val orders = repository.getUserOrders()
            orders.catch {
                _ordersList.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null ){
                    _ordersList.value= ResponseStatus.Success<OrdersResponse>(it)
                }else{
                    _ordersList.value = ResponseStatus.Error(NullPointerException("userOrders  is null"))
                }
            }

        }
    }

    fun getOrderDetails(orderId:Long){
        viewModelScope.launch {
            val orderDetails = repository.getSpecificOrderDetails(orderId)
            orderDetails.catch {
                _orderDetails.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null ){
                    _orderDetails.value= ResponseStatus.Success(it)
                }else{
                    _orderDetails.value = ResponseStatus.Error(NullPointerException("something went wrong"))
                }
            }
        }
    }

}

class OrdersFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrdersViewModel(repo) as T
    }
}
