package com.example.winkcart_user.cart.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.cart.view.components.CartItem
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrderRequest
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.utils.CustomButton
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING


@Composable
fun CartView(viewModel: CartViewModel) {

    val currencyCodeSaved by viewModel.currencyCode.collectAsState()
    val currencyRateSaved by viewModel.currencyRate.collectAsState()
    val draftOrders by viewModel.draftOrders.collectAsState()
    val customerID by viewModel.customerID.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()

    viewModel.refreshTotalAmount()
    viewModel.readCustomerID()
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


       ) {

           if (draftOrderList.isNotEmpty()) {
               LazyColumn(
                   modifier = Modifier.weight(1f),
               ) {
                   items(draftOrderList.size) { index ->
                       CartItem(
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

                               viewModel.refreshTotalAmount()

                               viewModel.updateDraftOrder(
                                   updatedDraftOrder.id,
                                   updatedDraftOrderRequest
                               )
                           }
                       )

                   }
               }
           } else {
               Box(
                   modifier = Modifier
                       .fillMaxSize(),
                   contentAlignment = Alignment.Center
               ) {
                   //need update with lotti or image for empty cart
                   Column(horizontalAlignment = Alignment.CenterHorizontally) {
                       Text(
                           "  All products from this vendor are currently out of stock. ",
                           style = MaterialTheme.typography.bodyMedium,
                           color = Color.Gray
                       )
                       Text(
                           " Stay tuned for updates!", style = MaterialTheme.typography.bodyMedium,
                           color = Color.Gray
                       )
                   }


               }
           }


           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(top = 8.dp),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ) {
               Text(
                   text = stringResource(R.string.total_amount),
                   color = Color.Gray,
                   fontSize = 24.sp,
               )
               Text(
                   text = totalAmount,
                   fontWeight = FontWeight.Bold,
                   fontSize = 24.sp,
               )

           }

           Spacer(Modifier.height(30.dp))

           CustomButton(lable = stringResource(R.string.check_out)){

           }
           Spacer(Modifier.height(10.dp))
       }

   }

}
