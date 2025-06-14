package com.example.winkcart_user.settings.view.currency

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.settings.enums.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectionSheet(
    currencyCodeSaved: String,
    onCurrencySelected: (Currency) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.select_currency),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(Currency.getAll()) { currency ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                onCurrencySelected(currency)
                                onDismissRequest()
                            }
                    ) {
                        RadioButton(
                            selected = currency.name == currencyCodeSaved,
                            onClick = {
                                onCurrencySelected(currency)
                                onDismissRequest()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(currency.name, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
