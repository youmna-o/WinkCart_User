package com.example.winkcart_user.cart.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.winkcart_user.R
import com.example.winkcart_user.data.model.draftorder.cart.DraftOrder
import com.example.winkcart_user.utils.CurrencyConversion.convertCurrency

@Composable
fun CartItem(
    draftOrder: DraftOrder,
    currencyRate:String,
    currencyCode:String,
    onDeleteClick: (Long) -> Unit,
    onQuantityChange: (DraftOrder, Int) -> Unit
) {

    val price = draftOrder.line_items[0]?.let {
        convertCurrency(
            amount = it.price,
            rate = currencyRate,
            currencyCode = currencyCode
        )
    }

    var quantity by remember {
        mutableIntStateOf(draftOrder.line_items[0]?.quantity ?: 1)
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete this item from the cart?") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick(draftOrder.id)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Row(
                modifier = Modifier
                    // .padding(8.dp)
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model =  draftOrder.line_items[0]?.properties?.get(3)?.value,
                    contentDescription = "product image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Color.Gray)
                )

                Spacer(Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                draftOrder.line_items[0]?.let {
                                    Text(
                                        text = it.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.widthIn(max = 200.dp)
                                    )
                                }

                                Spacer(Modifier.weight(1f))


                                IconButton(
                                    onClick = {  showDeleteDialog = true},
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = "Delete",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }

                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.color),
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                )
                                draftOrder.line_items[0]?.properties?.get(0)?.let {
                                    Text(
                                        text = it.value,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(Modifier.width(10.dp))

                                Text(
                                    text = stringResource(R.string.size),
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                )
                                draftOrder.line_items[0]?.properties?.get(1)?.let {
                                    Text(
                                        text = it.value,
                                        fontSize = 12.sp
                                    )
                                }


                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 4.dp,
                        ) {
                            val decreaseEnabled = quantity > 1

                            IconButton(
                                onClick = {

                                    if (decreaseEnabled) {
                                        quantity--
                                        onQuantityChange(draftOrder, quantity)
                                    }
                                },
                                enabled = decreaseEnabled,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Remove,
                                    contentDescription = "Decrease",
                                    tint = Color.Gray.copy(alpha = if (decreaseEnabled) 1f else 0.3f),
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(15.dp))

                        Text(
                            text = quantity.toString(),
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold

                        )
                        Spacer(Modifier.width(15.dp))

                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 4.dp,
                        ) {
                            val increaseEnabled = quantity < (draftOrder.line_items[0]?.properties?.get(2)?.value?.toInt()
                                ?: 10)

                            IconButton(
                                onClick = {
                                    if(increaseEnabled) {
                                        quantity++
                                        onQuantityChange(draftOrder, quantity)
                                    }
                                }
                                ,
                                enabled = increaseEnabled,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Increase",
                                    tint = Color.Gray.copy(alpha = if (increaseEnabled) 1f else 0.3f),
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "$price $currencyCode",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold

                        )

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                }
            }


        }
    }
}
