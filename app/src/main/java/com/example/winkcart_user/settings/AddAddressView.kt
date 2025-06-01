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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.utils.CustomButton
import com.example.winkcart_user.ui.utils.CustomTextField
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING

@Preview
@Composable
fun AddAddressView() {

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

//            CustomTextField(stringResource(R.string.title))
//            CustomTextField(stringResource(R.string.country))
//            CustomTextField(stringResource(R.string.address))
//            CustomTextField(stringResource(R.string.contact_person))
//            CustomTextField(stringResource(R.string.phone_number))
//            Spacer(modifier = Modifier.height(30.dp))
//            CustomButton(lable = "SAVE ADDRESS") { }


        }
    }
}