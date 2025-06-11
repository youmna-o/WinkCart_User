package com.example.winkcart_user.settings.view.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.data.model.settings.Address
import com.example.winkcart_user.ui.utils.AddFAB
import com.example.winkcart_user.ui.theme.BackgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressView(
    addAction : ()-> Unit,
    backAction : () -> Unit
) {
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

            AddressCard(Address(title = "Home", country = "Egypt", address = "Chino Hills, CA 91709, United States", phone = "+20 114 070 8568", contactPerson = "Rofaida Sobhy"))
            Spacer(modifier = Modifier.height(10.dp))
            AddressCard(Address(title = "Home", country = "Egypt", address = "Chino Hills, CA 91709, United States", phone = "+20 114 070 8568", contactPerson = "Rofaida Sobhy"))

            AddFAB(addAction)

        }
    }
        }
}