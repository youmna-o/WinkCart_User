package com.example.winkcart_user.ui.productInfo

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import com.example.winkcart_user.ui.productInfo.componants.FavIcon
import com.example.winkcart_user.ui.productInfo.componants.ImageSlider
import com.example.winkcart_user.ui.productInfo.componants.LongBasicDropdownMenu
import com.example.winkcart_user.ui.productInfo.componants.Reviews
import com.example.winkcart_user.ui.productInfo.componants.StarRatingBar
import com.example.winkcart_user.ui.utils.CustomButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInfo(
    productID: Long,
    navController: NavController,
    scrollState: ScrollState,
    viewModel: CategoriesViewModel
) {
    val productState = viewModel.products.collectAsState()

    var myProduct = remember(productState.value) {
        viewModel.getProduct(productID)
    }
    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Product Details",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Log.i("TAG", "ProductInfo: $productID")
            ImageSlider(myProduct?.images?.map { it.src }?.toList() ?: emptyList())
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically ) {
                LongBasicDropdownMenu(lable = "Size", myProduct?.options?.filter {
                    it.name == "Size"
                }?.flatMap { it.values }?.toList() ?: emptyList())
                LongBasicDropdownMenu(lable = "color", myProduct?.options?.filter {
                    it.name == "Color"
                }?.flatMap { it.values }?.toList() ?: emptyList())
                FavIcon()
            }
            Text("${myProduct?.title}", style = MaterialTheme.typography.titleLarge)
            Text("${myProduct?.variants?.get(0)?.price}$", style = MaterialTheme.typography.titleLarge)
            StarRatingBar(rating = 3.0f, size = 12.0f)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Text(myProduct?.body_html?:"", Modifier.padding(8.dp))
            }
            Reviews(
                rate = viewModel.getRate(),
                review = viewModel.getReview(),
                starSize = 8.0f
            )
            Row (horizontalArrangement = Arrangement.SpaceBetween){

                CustomButton(
                    lable = "ADD To CART"
                ) { }
            }

        }
    }
}



