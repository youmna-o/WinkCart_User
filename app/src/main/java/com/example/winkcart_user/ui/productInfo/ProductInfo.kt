package com.example.winkcart_user.ui.productInfo

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.CurrencyViewModel
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.Customer
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrder
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.data.model.draftorder.cart.LineItemDraft
import com.example.winkcart_user.data.model.draftorder.cart.Property
import com.example.winkcart_user.favourite.FavouriteViewModel
import com.example.winkcart_user.ui.categorie.categoriesViewModel.CategoriesViewModel
import com.example.winkcart_user.ui.productInfo.componants.ImageSlider
import com.example.winkcart_user.ui.productInfo.componants.LongBasicDropdownMenu
import com.example.winkcart_user.ui.productInfo.componants.Reviews
import com.example.winkcart_user.ui.productInfo.componants.StarRatingBar
import com.example.winkcart_user.ui.theme.myPurple
import com.example.winkcart_user.ui.utils.components.CustomButton
import com.example.winkcart_user.utils.CurrencyConversion.convertCurrency
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInfo(
    productID: Long,
    navController: NavController,
    scrollState: ScrollState,
    categoriesViewModel: CategoriesViewModel,
    cartViewModel: CartViewModel,
    currencyViewModel: CurrencyViewModel,
    favouriteViewModel: FavouriteViewModel,
) {

    currencyViewModel.readCurrencyCode()
    currencyViewModel.readCurrencyRate()
    val currencyRate = currencyViewModel.currencyRate.collectAsState().value
    val currencyCode = currencyViewModel.currencyCode.collectAsState().value


    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val customerID = cartViewModel.customerID.collectAsState()
    val productState = categoriesViewModel.products.collectAsState()
    val myProduct = remember(productState.value) {
        categoriesViewModel.getProduct(productID)
    }
    val draftOrders = favouriteViewModel.draftOrders.collectAsState()
    val matchedDraft = (draftOrders.value as? ResponseStatus.Success)?.result?.draft_orders
        ?.find { draft ->
            draft.line_items.any { it?.title == myProduct?.title }
        }

    val draftedOrders = favouriteViewModel.draftProductTitles.collectAsState()
    val isDraft = draftedOrders.value.contains(myProduct?.title)

    var selectedSize by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("") }


    var showLoginRequiredDialog by remember { mutableStateOf(false) }
    val selectedVariant = remember(selectedSize, selectedColor) {
        myProduct?.variants?.find { variant ->
            variant.option1 == selectedSize && variant.option2 == selectedColor
        }
    }
    if (showLoginRequiredDialog) {
        AlertDialog(
            onDismissRequest = { showLoginRequiredDialog = false },
            title = { Text("Guest Mode") },
            text = { Text("You can't use this feature, You must login first") },
            confirmButton = {
                TextButton(onClick = {
                    showLoginRequiredDialog = false
                    navController.navigate("login")
                }) {
                    Text("Login")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLoginRequiredDialog = false
                }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(Modifier.padding(16.dp),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E88E5)), // Custom background
                    containerColor = Color.Transparent, // Transparent to apply your own background
                    contentColor = Color.White,
                    action = {
                        Text(
                            text = data.visuals.actionLabel ?: "",
                            color = Color.Yellow,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { data.performAction() }
                        )
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = data.visuals.message)
                    }
                }
            }
        },
        topBar = {
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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Log.i("TAG", "ProductInfo: $productID")
            ImageSlider(myProduct?.images?.map { it.src }?.toList() ?: emptyList())
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically ) {
                LongBasicDropdownMenu(
                    lable = "Size",
                    menuItemData = myProduct?.options?.firstOrNull { it.name == "Size" }?.values ?: emptyList(),
                    selectedOption = selectedSize,
                    onOptionSelected = { selectedSize = it }
                )

                LongBasicDropdownMenu(
                    lable = "Color",
                    menuItemData = myProduct?.options?.firstOrNull { it.name == "Color" }?.values ?: emptyList(),
                    selectedOption = selectedColor,
                    onOptionSelected = { selectedColor = it }
                )


                IconButton(onClick = {
                    val idString = customerID.value
                    if (idString.isBlank()) {
                        showLoginRequiredDialog = true
                        return@IconButton
                    }

                    if (isDraft && matchedDraft != null) {
                            favouriteViewModel.deleteDraftOrder(matchedDraft.id)
                    } else if (myProduct != null && selectedVariant != null) {

                        val draftOrder = DraftOrderRequest(
                            draft_order = DraftOrder(
                                line_items = listOf(
                                    LineItemDraft(
                                        variant_id = selectedVariant.id,
                                        title = myProduct.title,
                                        price = selectedVariant.price,
                                        quantity = 1,
                                        properties = listOf(
                                            Property("Color", selectedColor),
                                            Property("Size", selectedSize),
                                            Property("Quantity_in_Stock", "${selectedVariant.inventory_quantity}"),
                                            Property("Image", myProduct.images[0].src),
                                            Property("SavedAt", "Favourite")
                                        ),
                                        product_id = 0
                                    )
                                ),
                                customer = Customer(idString.toLong())
                            )
                        )
                        favouriteViewModel.createDraftFavouriteOrder(idString, draftOrder)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite Icon",
                        tint = if (isDraft) myPurple else Color.Gray
                    )
                }


            }

            val convertedPrice = myProduct?.variants?.get(0)?.price?.let { convertCurrency(it, currencyRate, currencyCode).toFloatOrNull() }


            Text("${myProduct?.title}", style = MaterialTheme.typography.titleLarge)
            Text("$convertedPrice $currencyCode", style = MaterialTheme.typography.titleLarge)
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
                rate = categoriesViewModel.getRate(),
                review = categoriesViewModel.getReview(),
                starSize = 8.0f
            )

            CustomButton(
                lable = "ADD To CART"
            ) {
                val idString = customerID.value
                if (idString.isBlank()) {
                    showLoginRequiredDialog = true
                    return@CustomButton
                }

                if (myProduct != null && selectedVariant != null) {
                    val draftOrder = DraftOrderRequest(
                        draft_order = DraftOrder(
                            line_items = listOf(
                                LineItemDraft(
                                    variant_id = selectedVariant.id,
                                    title = myProduct.title,
                                    price = selectedVariant.price,
                                    quantity = 1,
                                    properties = listOf(
                                        Property("Color", selectedColor),
                                        Property("Size", selectedSize),
                                        Property("Quantity_in_Stock", "${selectedVariant.inventory_quantity}"),
                                        Property("Image", myProduct.images[0].src),
                                        Property("SavedAt", "Cart")
                                    ),
                                    product_id = 0
                                )
                            ),
                            customer = Customer(idString.toLong())
                        )
                    )
                    cartViewModel.createDraftCartOrder(idString,draftOrder)
                    Log.i("TAG", "ProductInfo: $draftOrder ")
                    Log.i("TAG", "customerID = ${customerID.value}")
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = "Added to cart successfully",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }

            cartViewModel.readCustomerID()
            Log.d("shared", "************ after auth")

        }
    }
}