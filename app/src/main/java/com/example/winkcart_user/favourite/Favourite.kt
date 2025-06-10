package com.example.winkcart_user.favourite

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.cart.view.components.CartItem
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favourite(viewModel: FavouriteViewModel) {

    val currencyCodeSaved by viewModel.currencyCode.collectAsState()
    val currencyRateSaved by viewModel.currencyRate.collectAsState()
    val draftOrders by viewModel.draftOrders.collectAsState()
    val customerID by viewModel.customerID.collectAsState()
    val scroll = rememberScrollState()


//    viewModel.readCustomerID()
    viewModel.getDraftOrders(customerId = customerID)

    viewModel.readCurrencyRate()
    viewModel.readCurrencyCode()

    val draftOrderList = when (draftOrders) {
        is ResponseStatus.Success -> (draftOrders as ResponseStatus.Success).result.draft_orders
        else -> emptyList()
    }

    Log.i("TAG", "CartView: draftOrders = ${draftOrderList.size}")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(SCREEN_PADDING)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)

        ) {
            if (draftOrderList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(draftOrderList.size) { index ->
                       FavItem(
                            draftOrder = draftOrderList[index],
                            currencyCode = currencyCodeSaved,
                            currencyRate = currencyRateSaved,
                            onDeleteClick = { draftOrderId ->
                                viewModel.deleteDraftOrder(draftOrderId)
                            },
                            onQuantityChange = { updatedDraftOrder, newQuantity ->
                                val updatedLineItem =
                                    updatedDraftOrder.line_items[0]?.copy(quantity = newQuantity)

                                val updatedDraftOrderRequest = DraftOrderRequest(
                                    draft_order = draftOrderList[index].copy(
                                        line_items = listOf(
                                            updatedLineItem
                                        )
                                    ),

                                    )
                            }
                        )

                    }
                }
            } else {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "  All products from this vendor are currently out of stock. ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            " Stay tuned for updates!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.height(30.dp))


        }

    }

}