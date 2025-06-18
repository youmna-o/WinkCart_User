package com.example.winkcart_user.settings.view.address

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.winkcart_user.R
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.settings.view.address.components.AddressCard
import com.example.winkcart_user.settings.view.address.components.NoAddressUI
import com.example.winkcart_user.ui.utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.ui.utils.components.AddFAB
import com.example.winkcart_user.theme.BackgroundColor
import com.example.winkcart_user.ui.utils.components.LottieAnimationView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressView(
    viewModel: SettingsViewModel=hiltViewModel(),
    addAction : ()-> Unit,
    backAction : () -> Unit,
    editAction : (Long, Long) -> Unit
) {
    val customerId by viewModel.customerID.collectAsState()
    viewModel.readCustomerID()
    Log.i("TAG", " Setting inti CustomerID: $customerId ")
    viewModel.getCustomerAddresses(customerId.toLong())

    val customerAddresses by viewModel.customerAddresses.collectAsState()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.address),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { backAction.invoke() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            AddFAB(addAction)
        }
    ) { paddingValues ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(paddingValues)
            .padding(SCREEN_PADDING)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (customerAddresses) {
                is ResponseStatus.Success -> {
                    val addresses = (customerAddresses as ResponseStatus.Success).result.addresses
                    if (addresses.isNotEmpty()) {
                        LazyColumn {
                            items(addresses.size) { index ->
                                AddressCard(
                                    customerId = customerId.toLong(),
                                    address = addresses[index],
                                    defaultCheckAction = { viewModel.setDefaultAddress(
                                        customerId = customerId.toLong(),
                                        addressId = addresses[index].id
                                        )
                                    },
                                    editAction = editAction,
                                    deleteAction = {
                                        viewModel.deleteCustomerAddress(customerId.toLong(), addresses[index].id)
                                    }

                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {

                            NoAddressUI()
                        }
                    }
                }
                is ResponseStatus.Loading -> {
                    LottieAnimationView(
                        animationRes = R.raw.animation_loading,
                        message = "Loading your cart..."
                    )
                }
                is ResponseStatus.Error -> {}/*ErrorUI()NoAddressUI()*/
            }
        }
    }
        }
}


