package com.example.winkcart_user.settings

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.example.winkcart_user.settings.view.currency.CurrencySelectionSheet
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.ui.theme.BackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    viewModel: SettingsViewModel,
    addressAction : ()-> Unit,
    contactUsAction : () -> Unit,
    aboutUsAction : () -> Unit,
    backAction : () -> Unit
) {

    val currencyCodeSaved by viewModel.currencyCode.collectAsState()

    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        CurrencySelectionSheet(
            currencyCodeSaved = currencyCodeSaved,
            onCurrencySelected = { selectedCurrency ->
                viewModel.writeCurrencyCode(selectedCurrency)
            },
            onDismissRequest = { showSheet = false }
        )
    }

    val settingsItems = listOf(
        Triple(R.drawable.ic_address, R.string.address, ""),
        Triple(R.drawable.ic_currency, R.string.currency, currencyCodeSaved),
        Triple(R.drawable.ic_contact_us, R.string.contact_us, ""),
        Triple(R.drawable.ic_about_us, R.string.about_us, "")
    )
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
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
                .padding(SCREEN_PADDING)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                settingsItems.forEach { (icon, title, subtitle) ->
                    SettingsCard(
                        settingIcon = icon,
                        settingName = title,
                        subtitleText = subtitle,
                        onClick = {
                            when (title) {
                                R.string.currency -> showSheet = true
                                R.string.address -> addressAction()
                                R.string.about_us -> aboutUsAction()
                                R.string.contact_us -> contactUsAction()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

            }
        }
    }
}
