package com.example.winkcart_user.ui.checkout.view.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft
import com.example.winkcart_user.data.model.orders.CustomerOrder
import com.example.winkcart_user.data.model.orders.OrderData
import com.example.winkcart_user.data.model.orders.OrderRequest
import com.example.winkcart_user.data.model.orders.OrdersResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CheckoutViewModel(private val repository: ProductRepo) : ViewModel() {

    private val _ordersResponse = MutableStateFlow<ResponseStatus<OrdersResponse>>(ResponseStatus.Loading)
    val ordersResponse = _ordersResponse.asStateFlow()

    fun createOrder(
        lineItems: List<LineItemDraft>
    ) {
        val request = OrderRequest(
            order = OrderData(
                customer = CustomerOrder(id = repository.readCustomersID().toLong()),
                line_items = lineItems,
                send_receipt = true
            )
        )
        viewModelScope.launch {
            val orders = repository.getUserOrders()
            orders.catch {
                _ordersResponse.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null ){
                    _ordersResponse.value= ResponseStatus.Success<OrdersResponse>(it)
                }else{
                    _ordersResponse.value = ResponseStatus.Error(NullPointerException("userOrders  is null"))
                }
            }
        }
    }


}

class CheckoutFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrdersViewModel(repo) as T
    }
}