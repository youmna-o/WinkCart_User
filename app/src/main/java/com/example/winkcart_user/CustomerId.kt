package com.example.winkcart_user
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CustomerIdViewModel(
    private val repo: ProductRepo
) : ViewModel() {

    private val _customerId = MutableStateFlow("")
    val customerId: StateFlow<String> = _customerId

    init {
        viewModelScope.launch {
            repo.readCustomerID().collect {
                _customerId.value = it
            }
        }
    }
}

class CustomerIdFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CustomerIdViewModel(repo) as T
    }
}

