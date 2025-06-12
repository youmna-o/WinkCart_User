package com.example.winkcart_user.settings.view.address

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.example.winkcart_user.settings.enums.Governorate

@Composable
fun GovernorateDropdown(
    selectedGovernorate: String,
    onGovernorateSelected: (Governorate) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    var inputText by remember(selectedGovernorate) { mutableStateOf(selectedGovernorate) }

    val filteredGovernorates = Governorate.getAll().filter {
        stringResource(it.stringResId).contains(inputText, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = inputText,
            onValueChange = {
                inputText = it
                expanded = true
            },
            label = { Text(text = stringResource(R.string.governorate)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                Row {
                    if (inputText.isNotEmpty()) {
                        IconButton(onClick = {
                            inputText = ""
                            expanded = false
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    } else {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }
                    }
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            if (filteredGovernorates.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.no_results_found)) },
                    onClick = {},
                    enabled = false
                )
            } else {
                filteredGovernorates.forEach { governorate ->
                    val name = stringResource(governorate.stringResId)
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            inputText = name
                            onGovernorateSelected(governorate)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
