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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import com.example.winkcart_user.data.model.draftorder.cart.Customer
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrder
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.LineItem
import com.example.winkcart_user.data.model.draftorder.cart.Property
import com.example.winkcart_user.ui.productInfo.componants.ImageSlider
import com.example.winkcart_user.ui.productInfo.componants.LongBasicDropdownMenu
import com.example.winkcart_user.ui.productInfo.componants.Reviews
import com.example.winkcart_user.ui.productInfo.componants.StarRatingBar
import com.example.winkcart_user.ui.utils.CustomButton

@Composable
fun ProductInfo(
    productID: Long,
    navController: NavController,
    scrollState: ScrollState,
    categoriesViewModel: CategoriesViewModel,
    cartViewModel: CartViewModel
) {
    val productState = categoriesViewModel.producs.collectAsState()
    val customerID = cartViewModel.customerID.collectAsState()

    var myProduct = remember(productState.value) {
        categoriesViewModel.getProduct(productID)
    }

    var selectedSize by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("") }

    val selectedVariant = remember(selectedSize, selectedColor) {
        myProduct?.variants?.find { variant ->
            variant.option1 == selectedSize && variant.option2 == selectedColor
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Log.i("TAG", "ProductInfo: $productID")
        ImageSlider(myProduct?.images?.map { it.src }?.toList() ?: emptyList())
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            LongBasicDropdownMenu(lable = "Size", myProduct?.options?.filter {
                it.name == "Size"
            }?.flatMap { it.values }?.toList() ?: emptyList(),
                onOptionSelected = { selectedSize = it }
            )

            LongBasicDropdownMenu(lable = "color", myProduct?.options?.filter {
                it.name == "Color"
            }?.flatMap { it.values }?.toList() ?: emptyList(),
                onOptionSelected = { selectedColor = it }
            )
        }
        Text("${myProduct?.title}", style = MaterialTheme.typography.titleLarge)
        Text("${myProduct?.variants?.get(0)?.price}$", style = MaterialTheme.typography.titleLarge)
        StarRatingBar(rating = 3.0f, size = 12.0f)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Text(myProduct?.body_html?:"", Modifier.padding(8.dp))
        }
        Reviews(
            rate = categoriesViewModel.getRate(),
            review = categoriesViewModel.getReview(),
            starSize = 8.0f
        )
        CustomButton(
            lable = "ADD To CART"
        ) {


            val draftOrder = DraftOrderRequest(
                draft_order = DraftOrder(
                    line_items = listOf(
                        myProduct?.let {
                            selectedVariant?.let { it1 ->
                                LineItem(
                                    variant_id = it1.id,
                                    title = it.title,
                                    price = it.variants[0].price,
                                    quantity = 1,
                                    properties = listOf(
                                        Property("Color", selectedColor),
                                        Property("Size", selectedSize),
                                        Property("Quantity_in_Stock", "${it1.inventory_quantity}"),
                                        Property("Image", myProduct.images[0].src),
                                        Property("SavedAt",  "Cart")
                                    )
                                )
                            }
                        }
                    ),
                    customer = Customer(customerID.value.toLong())
                )
            )
            cartViewModel.createDraftOrder(draftOrder)
            Log.i("TAG", "ProductInfo: $draftOrder ")
            Log.i("TAG", "customerID = ${customerID.value}")

        }

    }
}



