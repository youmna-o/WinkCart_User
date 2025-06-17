package com.example.winkcart_user.ui.home.vendorProducts.views


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductAbstracted
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel

import com.example.winkcart_user.ui.utils.components.ProductItem

import com.example.winkcart_user.ui.utils.navigation.CustomSearchBar


@Composable
fun VendorProductScreen(
    vendor: String,
    vendorProductsViewModel: VendorProductsViewModel= hiltViewModel(),
    navController: NavController
) {
    //var searchInput by remember { mutableStateOf("") }
    var isSearch = false
    vendorProductsViewModel.readCurrencyRate()
    vendorProductsViewModel.readCurrencyCode()
    vendorProductsViewModel.getProductsPyVendor(vendor)
   // vendorProductsViewModel.getSearchProducts(vendor,searchInput)
    val searchInput by vendorProductsViewModel.searchInput.collectAsState()
    var currencyCodeSaved = vendorProductsViewModel.currencyCode.collectAsState().value
    var currencyRateSaved = vendorProductsViewModel.currencyRate.collectAsState().value
    var productsByVendor = vendorProductsViewModel.productByVendor.collectAsState()

    var filteredProducts  = vendorProductsViewModel.filteredProducts.collectAsState().value

    when (productsByVendor.value) {
        is ResponseStatus.Loading -> {
            VendorProductScreenOnLoading()
        }

        is ResponseStatus.Success -> {

        VendorProductsOnScuccess(
            searchInput = searchInput,
            onSearchInputChange =
                {
                    vendorProductsViewModel.onSearchInputChanged(it) }

            ,
            products = mapProductsToBaAbstracted(filteredProducts),
            vendor = vendor,
            navController = navController,
            currencyCode = currencyCodeSaved,
            currencyRate = currencyRateSaved
        )
    }

        is ResponseStatus.Error -> {
            VendorProductScreenOnLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorProductsOnScuccess(  searchInput: String,
                               onSearchInputChange: (String) -> Unit,products: List<ProductAbstracted>, vendor: String, navController: NavController, currencyRate:String, currencyCode:String) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            CustomSearchBar(
                searchInput = searchInput,
                onSearchInputChange =  onSearchInputChange,
            )


            if (products.isNotEmpty()) {
                LazyColumn {
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

    }


@Composable
fun VendorProductScreenOnLoading() {
}

fun mapProductsToBaAbstracted(products: List<Product>): List<ProductAbstracted> {
    return products.map { product ->
        ProductAbstracted(
            title = product.title,
            imageUrl = product.image?.src ?: "",
            price = "${product.variants.firstOrNull()?.price ?: "0"}",
            id = product.id
        )
    }
}
