package com.example.winkcart_user.ui.home.main.brandsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.model.vendors.SmartCollectionsResponse
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.data.ResponseStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BrandsViewModel(private  val repo: ProductRepo) : ViewModel() {

    init {
       getSmartCollections()
    }

    private val _brandList = MutableStateFlow<ResponseStatus<SmartCollectionsResponse>>(ResponseStatus.Loading)
    val brandList = _brandList.asStateFlow()


    fun getSmartCollections() {
        viewModelScope.launch {
            repo.getFilteredSmartCollections()
                .catch { exception ->
                    _brandList.value = ResponseStatus.Error(exception)
                }
                .collect { response ->
                    if (response != null) {
                        _brandList.value = ResponseStatus.Success(response)
                    } else {
                        _brandList.value = ResponseStatus.Error(
                            NullPointerException("SmartCollectionsResponse is null")
                        )
                    }
                }
        }
    }
}
class BrandsFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BrandsViewModel(repo) as T
    }
}
