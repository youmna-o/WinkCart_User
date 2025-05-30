package com.example.winkcart_user.ui.home.vendorProducts.views


import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductAbstracted
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel

@Composable
fun VendorProductScreen(
    vendor: String, vendorProductsViewModel: VendorProductsViewModel = VendorProductsViewModel(
        ProductRepoImpl(
            RemoteDataSourceImpl(
                RetrofitHelper()
            )
        )
    )
) {
    vendorProductsViewModel.getProductsPyVendor(vendor)
    var productsByVendor = vendorProductsViewModel.productByVendor.collectAsState()
    when (productsByVendor.value) {
        is ResponseStatus.Loading -> {
            VendorProductScreenOnLoading()
        }

        is ResponseStatus.Success -> VendorProductsOnScuccess(
            mapProductsToBaAbstracted((productsByVendor.value as ResponseStatus.Success<ProductResponse>).result.products),
            vendor = vendor,
        )

        is ResponseStatus.Error -> {
            VendorProductScreenOnLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorProductsOnScuccess(products: List<ProductAbstracted>, vendor: String) {
    Scaffold(
        containerColor = Color(245, 245, 245),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "$vendor",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* handle back */ TODO() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }

    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(products.size) { index ->
                ProductItem(product = products[index])
            }
        }
    }
}

@Composable
fun VendorProductScreenOnLoading() {
}

fun mapProductsToBaAbstracted(products: List<Product>): List<ProductAbstracted> {
    Log.i("TAG", "mapProductsToBaAbstracted:${products.size} ")
    return products.map { product ->
        ProductAbstracted(
            title = product.title,
            imageUrl = product.image?.src ?: "",
            price = "${product.variants.firstOrNull()?.price ?: "0"} EG",
            id = product.id
        )
    }
}
