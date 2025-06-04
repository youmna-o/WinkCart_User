package com.example.winkcart_user.categories.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.repository.ProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriesViewModel (private val repo: ProductRepo ) :ViewModel() {

    private val _productList = MutableStateFlow<ResponseStatus<ProductResponse>>(ResponseStatus.Loading)
    val products = _productList.asStateFlow()

    private val _searchInput = MutableStateFlow("")
    val searchInput = _searchInput.asStateFlow()

    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())

    init {
        getAllProducts()
    }

    val filteredProducts = combine(_filteredProducts, _searchInput) { products, search ->
        if (search.isBlank()) products
        else products.filter { it.title.contains(search, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onSearchInputChanged(input: String) {
        _searchInput.value = input
    }
    fun getAllProducts() {
        viewModelScope.launch {
            val products = repo.getAllProducts()
            products.catch {
                _productList.value = ResponseStatus.Error(it)
            }.collect{ it
                if (it!= null){
                    _productList.value= ResponseStatus.Success<ProductResponse>(it)
                    _filteredProducts.value = it.products
                }else{
                    _productList.value = ResponseStatus.Error(NullPointerException("SmartCollectionsResponse is null"))
                }
            }
        }
    }

    fun getProductsList() : List<Product>? {
        val productResponse: ProductResponse? = (_productList.value as? ResponseStatus.Success<ProductResponse>)?.result
        val productList = productResponse?.products
        return productList
    }

    fun getProduct(id: Long): Product? {
        val productResponse = (_productList.value as? ResponseStatus.Success<ProductResponse>)?.result
        val productList = productResponse?.products
        return productList?.find { it.id == id }
    }

    fun getRate(): Float {
        return repo.getRate()
    }
     fun getReview(): String {
        return repo.getReview()
    }



    fun getALlSubCategories() : Set<String> {
        val productResponse: ProductResponse? = (_productList.value as? ResponseStatus.Success<ProductResponse>)?.result
        val uniqueProductTypes: Set<String> = productResponse?.products
            ?.map { it.product_type }
            ?.toSet()
            ?: emptySet()
        return uniqueProductTypes
    }

//    fun getProductDetails() : Product {
//        val productResponse: ProductResponse? = (_productList.value as? ResponseStatus.Success<ProductResponse>)?.result
//       // val
////        val uniqueProductTypes: Set<String> = productResponse?.products
////            ?.map { it.product_type }
////            ?.toSet()
////            ?: emptySet()
////        return uniqueProductTypes
//   // }


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
class CategoryFactory(private  val repo: ProductRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriesViewModel(repo) as T
    }
}
