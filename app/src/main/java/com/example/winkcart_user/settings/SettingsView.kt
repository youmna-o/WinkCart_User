package com.example.winkcart_user.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.Utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor

@Preview
@Composable
fun SettingsView() {

    val settingsItems = listOf(
        R.drawable.ic_address to R.string.address,
        R.drawable.ic_currency to R.string.currency,
        R.drawable.ic_contact_us to R.string.contact_us,
        R.drawable.ic_about_us to R.string.about_us,
    )

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

            Text(text=stringResource(R.string.settings),color = HeaderTextColor , fontSize = 32.sp, fontWeight = FontWeight.Bold )
            Spacer(modifier = Modifier.height(30.dp))

            settingsItems.forEach { (icon, title) ->
                SettingsCard(settingIcon = icon, settingName = title)
                Spacer(modifier = Modifier.height(10.dp))
            }

        }
    }
}
