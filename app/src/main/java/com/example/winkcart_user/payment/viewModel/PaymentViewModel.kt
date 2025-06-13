package com.example.winkcart_user.payment.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.payment.model.CardFormValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
            cvvError = cvv.isBlank()
        )

        _formValidationState.value = validation

        return with(validation) {
            !nameOnCartError && !cardNumberError && !expireDateError && !cvvError
        }
    }

}

class PaymentFactory(private val repo: ProductRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return PaymentViewModel(repo) as T
    }
}