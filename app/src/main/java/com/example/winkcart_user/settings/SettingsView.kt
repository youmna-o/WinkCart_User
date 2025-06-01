package com.example.winkcart_user.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsView(viewModel: SettingsViewModel) {

    val currencyCodeSaved by viewModel.currencyCode.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.select_currency), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                val currencies = listOf("EGP", "USD")

                currencies.forEach { currency ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                viewModel.writeCurrencyCode(currency)
                                showSheet = false
                            }
                    ) {
                        RadioButton(
                            selected = currency == currencyCodeSaved,
                            onClick = {
                                viewModel.writeCurrencyCode(currency)
                                showSheet = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(currency, fontSize = 18.sp)
                    }
                }
            }
        }
    }


    val settingsItems = listOf(
        Triple(R.drawable.ic_address, R.string.address, ""),
        Triple(R.drawable.ic_currency, R.string.currency, currencyCodeSaved),
        Triple(R.drawable.ic_contact_us, R.string.contact_us, ""),
        Triple(R.drawable.ic_about_us, R.string.about_us, "")
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

            settingsItems.forEach {  (icon, title, subtitle) ->
                SettingsCard(
                    settingIcon = icon,
                    settingName = title,
                    subtitleText = subtitle,
                    onClick = {
                        if (title == R.string.currency) {
                            coroutineScope.launch {
                                showSheet = true
                                sheetState.show()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

        }
    }
}
