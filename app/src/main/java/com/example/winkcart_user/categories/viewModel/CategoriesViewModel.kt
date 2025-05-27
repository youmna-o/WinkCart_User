package com.example.winkcart_user.categories.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoriesViewModel (private val repo: ProductRepo ) :ViewModel() {

    private val _productList = MutableStateFlow<ResponseStatus<ProductResponse>>(ResponseStatus.Loading)
    val producs = _productList.asStateFlow()




    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            val products = repo.getAllProducts()
            products.catch {
                _productList.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null){
                    _productList.value= ResponseStatus.Success<ProductResponse>(it)
                }else{
                    _productList.value = ResponseStatus.Error(NullPointerException("SmartCollectionsResponse is null"))
                }
            }
        }
    }


    fun getALlSubCategories() : Set<String> {
        val productResponse: ProductResponse? = (_productList.value as? ResponseStatus.Success<ProductResponse>)?.result
        val uniqueProductTypes: Set<String> = productResponse?.products
            ?.map { it.product_type }
            ?.toSet()
            ?: emptySet()
        return uniqueProductTypes
    }


    fun getMenProducts(): List<Product> {
        val productResponse = (_productList.value as? ResponseStatus.Success<ProductResponse>)?.result
        return productResponse?.products
            ?.filter { it.tags.contains("men", ignoreCase = true) }
            ?: emptyList()
    }

    fun getWomenProducts(): List<Product>{
        val productResponse = (_productList.value as? ResponseStatus.Success<ProductResponse>)?.result
        return productResponse?.products
            ?.filter { it.tags.contains("women", ignoreCase = true) }
            ?: emptyList()
    }
    fun getKidsProducts():List<Product>{
        val productResponse = (_productList.value as? ResponseStatus.Success<ProductResponse>)?.result
        return productResponse?.products
            ?.filter { it.tags.contains("kid", ignoreCase = true) }
            ?: emptyList()
    }

}