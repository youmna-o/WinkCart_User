package com.example.winkcart_user.ui.checkout.view.viewModel

import android.util.Log
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
import com.example.winkcart_user.payment.model.CardFormValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PaymentViewModel(private  val repo: ProductRepo) : ViewModel() {

    private val _formValidationState = MutableStateFlow(CardFormValidationState())
    val formValidationState = _formValidationState.asStateFlow()

    fun validateCardForm(
        nameOnCard: String,
        cardNumber: String,
        expireDate: String,
        cvv: String,
    ): Boolean {

        val validation = CardFormValidationState(
            nameOnCartError = nameOnCard.isBlank(),
            cardNumberError = cardNumber.isBlank() || cardNumber.length != 16,
            expireDateError = expireDate.isBlank(),
            cvvError = cvv.isBlank()|| cvv.length != 3
        )

        _formValidationState.value = validation

        return with(validation) {
            !nameOnCartError && !cardNumberError && !expireDateError && !cvvError
        }
    }


    private val _ordersResponse = MutableStateFlow<ResponseStatus<OrdersResponse>>(ResponseStatus.Loading)
    val ordersResponse = _ordersResponse.asStateFlow()

    fun createOrder(
        lineItems: List<LineItemDraft>
    ) {
        val request = OrderRequest(
            order = OrderData(
                customer = CustomerOrder(id =repo.readCustomersID().toLong()),
                line_items = lineItems,
                send_receipt = true
            )
        )
        viewModelScope.launch {
            val orders = repo.createOrder(request)
            Log.i("TAG", "createOrder: ${orders}")
            orders.catch {
                _ordersResponse.value = ResponseStatus.Error(it)
                Log.i("TAG", "createOrder error: ${orders}")
            }.collect{ it
                if (it!= null ){
                    _ordersResponse.value= ResponseStatus.Success<OrdersResponse>(it)
                    Log.i("TAG", "createOrder value : ${orders}")
                }else{
                    _ordersResponse.value = ResponseStatus.Error(NullPointerException("userOrders  is null"))
                    Log.i("TAG", "createOrdernull: ${orders}")
                }

            }
        }
    }


}

class PaymentFactory(private val repo: ProductRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return PaymentViewModel(repo) as T
    }
}