package com.example.winkcart_user.settings.view.address

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.ui.utils.AddFAB
import com.example.winkcart_user.ui.theme.BackgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressView(
    viewModel: SettingsViewModel,
    addAction : ()-> Unit,
    backAction : () -> Unit
) {
    val customerId by viewModel.customerID.collectAsState()
    LaunchedEffect(customerId) {
        viewModel.getCustomerAddresses(customerId.toLong())
    }
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
                                AddressCard(address = addresses[index])
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            NoAddressUI()
                        }
                    }
                }
                is ResponseStatus.Loading -> /*LoadingUI()*/NoAddressUI()
                is ResponseStatus.Error -> /*ErrorUI()*/NoAddressUI()
            }
            AddFAB(addAction)
        }
    }
        }
}

@Composable
fun NoAddressUI() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            stringResource(R.string.you_have_not_added_any_addresses_yet),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            stringResource(R.string.add_a_new_address_to_make_delivery_easier),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
