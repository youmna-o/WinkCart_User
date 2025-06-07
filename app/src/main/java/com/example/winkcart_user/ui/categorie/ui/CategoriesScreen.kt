
package com.example.winkcart_user.ui.categorie.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.products.Product
import com.example.winkcart_user.data.model.products.ProductResponse
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.CurrencyViewModel
import com.example.winkcart_user.data.model.products.ProductAbstracted
import com.example.winkcart_user.ui.utils.ProductItem
import androidx.compose.runtime.mutableStateOf
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.winkcart_user.utils.CurrencyConversion.convertCurrency


@Composable
fun CategoriesScreen (categoriesViewModel: CategoriesViewModel,
                      navController: NavController,
                      currencyViewModel: CurrencyViewModel){

    val allProducts by  categoriesViewModel.products.collectAsState()
    categoriesViewModel.getMenProducts()
    categoriesViewModel.getMenProducts()
    categoriesViewModel.getKidsProducts()
    categoriesViewModel.getWomenProducts()

    when (allProducts) {
        is ResponseStatus.Success<*> ->{
            CategoriesScreenOnSuccess(categoriesViewModel,navController,currencyViewModel)
        }
        is ResponseStatus.Loading ->{
            CategoriesScreenonLoading()
        }

        is ResponseStatus.Error -> {
         CategoriesScreenOnError(onRetry = {categoriesViewModel.getAllProducts()})
        }
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreenOnSuccess(categoriesViewModel: CategoriesViewModel,
                              navController: NavController,
                              currencyViewModel: CurrencyViewModel) {
    currencyViewModel.readCurrencyCode()
    currencyViewModel.readCurrencyRate()
    categoriesViewModel.getMenProducts()
    categoriesViewModel.getWomenProducts()
    categoriesViewModel.getKidsProducts()
    val currencyRate = currencyViewModel.currencyRate.collectAsState().value
    val currencyCode = currencyViewModel.currencyCode.collectAsState().value
    val subcategories = categoriesViewModel.getALlSubCategories()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedSubcategoryIndex by remember { mutableIntStateOf(-1) }
    var isFilterAppliedState by remember { mutableStateOf(false) }



    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Categories",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val rowData: List<Product> = when (selectedTabIndex) {
            0 -> (categoriesViewModel.products.value as ResponseStatus.Success<ProductResponse>).result.products
            1 -> categoriesViewModel.getWomenProducts()
            2 -> categoriesViewModel.getMenProducts()
            else -> categoriesViewModel.getKidsProducts()
        }
        val subcategoryList = subcategories.toList()
        var baseList : List<Product> = if (selectedSubcategoryIndex != -1) {
            val selectedSubcategory = subcategoryList[selectedSubcategoryIndex]
            rowData.filter { it.product_type == selectedSubcategory }
        } else {
            rowData
        }
        var currentList by remember { mutableStateOf(baseList) }

        LaunchedEffect(baseList) {
            currentList = baseList
            isFilterAppliedState = false
        }

        Column(modifier = Modifier.padding(paddingValues)) {
            CategoryTabs(
                selectedTabIndex = selectedTabIndex,
                subcategories = subcategories,
                selectedSubcategory = selectedSubcategoryIndex,
                onTabSelected = { selectedTabIndex = it
                    isFilterAppliedState = false },
                onSubcategorySelected = { selectedSubcategoryIndex = it
                    isFilterAppliedState = false}
            )

            FilterSortRow(
                onApplyFilter = { min, max ->
                    currentList = baseList.filter { product ->
                        val priceStr = product.variants[0].price
                        val convertedPrice = convertCurrency(priceStr, currencyRate, currencyCode).toFloatOrNull()
                        convertedPrice != null && convertedPrice in min..max}
                   isFilterAppliedState = true
                },
                onResetFilter = {
                    currentList = baseList
                    isFilterAppliedState = false
                },
                isResetEnabled = isFilterAppliedState,
            )
            CategoryProducts(
                currentList,
                currencyRate = currencyRate,
                currencyCode = currencyCode,
                navController = navController,
            )
        }
    }
}
@Composable
fun CategoryProducts(Products :List<Product>,currencyRate:String,
                     currencyCode:String,navController: NavController) {
    if (Products.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 75.dp),
        ) {
            items(Products.size) { index ->
                ProductItem(
                    product = ProductAbstracted(
                        id = Products[index].id,
                        title = Products[index].title,
                        price = Products[index].variants[0].price,
                        imageUrl = Products[index].image?.src?:""
                    ) ,
                    currencyCode = currencyCode,
                    currencyRate = currencyRate,
                    onProductItemClicked = {navController
                        .navigate("ProductInfo/${Products[index].id}") }
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("  All products in this section are currently out of stock. ",
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

@Composable
fun CategoriesScreenonLoading(){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Loading categories...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun CategoriesScreenOnError(onRetry: () -> Unit = {}){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun CategoryTabs(
    selectedTabIndex: Int,
    subcategories : Set<String> ,
    onTabSelected: (Int) -> Unit ,
    selectedSubcategory: Int,
    onSubcategorySelected :(Int) -> Unit
) {
    val categories = listOf( "ALL","Women", "Men", "Kids")

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            LazyRow(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subcategories.toList().size ) { indxe ->
                    FilterChip(
                        selected =   selectedSubcategory == indxe,
                        onClick = {
                            onSubcategorySelected(if (selectedSubcategory == indxe) -1 else indxe)
                        },
                        label = { Text(subcategories.toList()[indxe] ) },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }

}
@Composable
fun FilterSortRow(onApplyFilter: (minPrice: Float, maxPrice: Float) -> Unit,
    onResetFilter: () -> Unit, isResetEnabled :Boolean ) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .clickable { showBottomSheet = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter by price",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Filters", style = MaterialTheme.typography.bodyMedium)
        }

        TextButton(
            onClick = { onResetFilter()
                   },
            enabled = isResetEnabled,
        ) {
            Text("reset filter")
        }
    }

    if (showBottomSheet) {
        BottomSheetFilter(
            onApplyFilter = { minPrice, maxPrice ->
                onApplyFilter(minPrice, maxPrice)
                showBottomSheet = false
            },
            onDismiss = { showBottomSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFilter(
    onApplyFilter: (minPrice: Float, maxPrice: Float) -> Unit,
    onDismiss: () -> Unit
) {
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }
    var minPriceError by remember { mutableStateOf<String?>(null) }
    var maxPriceError by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Filter by Price", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = minPrice,
                    onValueChange = {
                        minPrice = it
                        minPriceError = null
                    },
                    label = { Text("Min Price") },
                    placeholder = { Text("Enter min price") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = minPriceError != null,
                    supportingText = {
                        minPriceError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = maxPrice,
                    onValueChange = {
                        maxPrice = it
                        maxPriceError = null
                    },
                    label = { Text("Max Price") },
                    placeholder = { Text("Enter max price") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = maxPriceError != null,
                    supportingText = {
                        maxPriceError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val min = minPrice.toFloatOrNull()
                    val max = maxPrice.toFloatOrNull()

                    minPriceError = null
                    maxPriceError = null

                    var isValid = true

                    if (minPrice.isBlank()) {
                        minPriceError = "Min price is required"
                        isValid = false
                    } else if (min == null) {
                        minPriceError = "Invalid number"
                        isValid = false
                    }

                    if (maxPrice.isBlank()) {
                        maxPriceError = "Max price is required"
                        isValid = false
                    } else if (max == null) {
                        maxPriceError = "Invalid number"
                        isValid = false
                    }

                    if (isValid && min != null && max != null) {
                        if (min > max) {
                        maxPriceError = "Max price should be greater than Min price"
                        } else {
                            onApplyFilter(min, max)
                            onDismiss()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filter")
            }
        }
    }
}

