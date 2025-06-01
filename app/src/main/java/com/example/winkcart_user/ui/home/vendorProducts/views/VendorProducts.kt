package com.example.winkcart_user.ui.home.vendorProducts.views


import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.local.LocalDataSourceImpl
import com.example.winkcart_user.data.local.settings.SettingsDaoImpl
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductAbstracted
import com.example.winkcart_user.data.model.products.ProductResponse
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel

@Composable
fun VendorProductScreen(
    vendor: String,
    vendorProductsViewModel: VendorProductsViewModel ,
    navController: NavController
) {
    vendorProductsViewModel.readCurrencyRate()
    vendorProductsViewModel.readCurrencyCode()
    vendorProductsViewModel.getProductsPyVendor(vendor)
    var currencyCodeSaved = vendorProductsViewModel.currencyCode.collectAsState().value
    var currencyRateSaved = vendorProductsViewModel.currencyRate.collectAsState().value
    var productsByVendor = vendorProductsViewModel.productByVendor.collectAsState()
    when (productsByVendor.value) {
        is ResponseStatus.Loading -> {
            VendorProductScreenOnLoading()
        }
        is ResponseStatus.Success -> VendorProductsOnScuccess(
            mapProductsToBaAbstracted((productsByVendor.value as ResponseStatus.Success<ProductResponse>).result.products),
            vendor = vendor,
            navController,
            currencyCode = currencyCodeSaved,
            currencyRate = currencyRateSaved
        )

        is ResponseStatus.Error -> {
            VendorProductScreenOnLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorProductsOnScuccess(products: List<ProductAbstracted>, vendor: String, navController: NavController, currencyRate:String, currencyCode:String) {
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
                    IconButton(onClick = { navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }

    ) { padding ->
        if (products.isNotEmpty()) {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(products.size) { index ->
                    ProductItem(
                        product = products[index],
                        currencyCode = currencyCode,
                        currencyRate = currencyRate,
                        onProductItemClicked = { navController.navigate("ProductInfo/${products[index].id}") }

                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("  All products from this vendor are currently out of stock. ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(" Stay tuned for updates!",style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }



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
            price = "${product.variants.firstOrNull()?.price ?: "0"}",
            id = product.id
        )
    }
}
