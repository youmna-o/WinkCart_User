package com.example.winkcart_user.ui.home.vendorProducts.views


import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    brand: String, vendorProductsViewModel: VendorProductsViewModel = VendorProductsViewModel(
        ProductRepoImpl(
            RemoteDataSourceImpl(
                RetrofitHelper()
            )
        )
    )
) {
    vendorProductsViewModel.getProductsPyVendor(brand)
    var productsByVendor = vendorProductsViewModel.productByVendor.collectAsState()
    when (productsByVendor.value) {
        is ResponseStatus.Loading -> {
            VendorProductScreenOnLoading()
        }

        is ResponseStatus.Success -> VendorProductsOnScuccess(
            mapProductsToBaAbstracted((productsByVendor.value as ResponseStatus.Success<ProductResponse>).result.products),
            brand = brand,
        )

        is ResponseStatus.Error -> {
            VendorProductScreenOnLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorProductsOnScuccess(products: List<ProductAbstracted>, brand: String) {
    Scaffold(
        containerColor = Color(245, 245, 245),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "$brand",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* handle back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // or your preferred color
                )
            )
        }

    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(products.size) { index ->
                SecondProductItem(product = products[index])
            }
        }
    }
}

@Composable
fun VendorProductScreenOnLoading() {
TODO()
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
