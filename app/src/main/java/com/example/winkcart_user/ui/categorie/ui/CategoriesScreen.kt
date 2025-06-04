package com.example.winkcart_user.ui.categorie.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.CurrencyViewModel
import com.example.winkcart_user.data.model.products.ProductAbstracted
import com.example.winkcart_user.ui.utils.ProductItem

@Composable
fun CategoriesScreen (categoriesViewModel: CategoriesViewModel, navController: NavController, currencyViewModel: CurrencyViewModel){

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
         CategoriesScreenOnError()
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
    var selectedTabIndex by remember { mutableStateOf(0) }
    var selectedSubcategoryIndex by remember { mutableStateOf(-1) }


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
        val baseList: List<Product> = when (selectedTabIndex) {
            0 -> (categoriesViewModel.products.value as ResponseStatus.Success<ProductResponse>).result.products
            1 -> categoriesViewModel.getWomenProducts()
            2 -> categoriesViewModel.getMenProducts()
            else -> categoriesViewModel.getKidsProducts()
        }
        val subcategoryList = subcategories.toList()
        val currentList: List<Product> = if (selectedSubcategoryIndex != -1) {
            val selectedSubcategory = subcategoryList[selectedSubcategoryIndex]
            baseList.filter { it.product_type == selectedSubcategory }
        } else {
            baseList
        }




        Column(modifier = Modifier.padding(paddingValues)) {
            CategoryTabs(
                selectedTabIndex = selectedTabIndex,
                subcategories = subcategories,
                selectedSubcategory = selectedSubcategoryIndex,
                onTabSelected = { selectedTabIndex = it },
                onSubcategorySelected = { selectedSubcategoryIndex = it }
            )


            FilterSortRow(
                selectedSortOption = "Price: lowest to high",
                onFilterClick = { /* filter sheet */ },
                onSortClick = { /* sort options */ },
                onToggleViewClick = { /* toggle list */ }
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
            modifier = Modifier.fillMaxSize()
                 ,
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
                    onProductItemClicked = { navController.navigate("ProductInfo/${Products[index].id}") }

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
    Column {
//to do
    }
}

@Composable
fun CategoriesScreenOnError(){
    Column {
// to do
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
    var selectedSubcategoryIndex = selectedSubcategory
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
fun FilterSortRow(
    selectedSortOption: String,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    onToggleViewClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .clickable { onFilterClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Filters", style = MaterialTheme.typography.bodyMedium)
        }

        Row(
            modifier = Modifier
                .clickable { onSortClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = "Sort",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = selectedSortOption,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(onClick = onToggleViewClick) {
            Icon(
                imageVector = Icons.Default.GridView,
                contentDescription = "Toggle View",
                tint = Color.Black
            )
        }
    }
}

