package com.example.winkcart_user.ui.utils.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
@Composable
fun CustomTextField(
    lable: String,
    input: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = input,
            onValueChange = onValueChange,
            label = { Text(lable) },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun CustomSmallTextField(
    lable: String,
    input: String,
    onValueChange: (String) -> Unit,
    isEmailError: Boolean,

    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = input,
        onValueChange = onValueChange,
        label = { Text(lable) },
        isError = isEmailError,
        singleLine = true,
        modifier = modifier
            .padding(bottom = 8.dp)
    )
}
