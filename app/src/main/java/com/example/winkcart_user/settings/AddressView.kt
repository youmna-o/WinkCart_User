package com.example.winkcart_user.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.data.model.settings.Address
import com.example.winkcart_user.ui.theme.BackgroundColor

@Preview
@Composable
fun AddressView() {

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

            AddressCard(Address(title = "Home", country = "Egypt", address = "Chino Hills, CA 91709, United States", phone = "+20 114 070 8568", contactPerson = "Rofaida Sobhy"))
            Spacer(modifier = Modifier.height(10.dp))
            AddressCard(Address(title = "Home", country = "Egypt", address = "Chino Hills, CA 91709, United States", phone = "+20 114 070 8568", contactPerson = "Rofaida Sobhy"))

            AddFAB()

        }
    }
}