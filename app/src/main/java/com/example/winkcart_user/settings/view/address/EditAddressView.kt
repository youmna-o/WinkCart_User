package com.example.winkcart_user.settings.view.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.winkcart_user.R
import com.example.winkcart_user.data.ResponseStatus
import com.example.winkcart_user.data.model.settings.address.CustomerAddress
import com.example.winkcart_user.data.model.settings.address.CustomerAddressRequest
import com.example.winkcart_user.helperclasses.MapUtils
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.theme.BackgroundColor
import com.example.winkcart_user.theme.myPurple
import com.example.winkcart_user.ui.utils.components.CustomButton
import com.example.winkcart_user.ui.utils.Constants.SCREEN_PADDING
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressView(
    customerId: Long,
    addressId: Long,
    viewModel: SettingsViewModel=hiltViewModel(),
    backAction: () -> Unit,
    navigateToMapAction:()->Unit,
    addressLatLon : LatLng?
) {
    val validationState by viewModel.formValidationState.collectAsState()
    val customerAddress by viewModel.customerAddress.collectAsState()

    val savedCountry = stringResource(R.string.egypt)

    var title by remember { mutableStateOf("") }
    //var selectedGovernorate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactPerson by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var country by remember { mutableStateOf(savedCountry) }
    var isOutOfCoverage by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Update Address") },
            text = { Text("Are you sure you want to save these changes?") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        showDialog = false
                        val request = CustomerAddressRequest(
                            CustomerAddress(
                                title = title,
                                city = ""/*selectedGovernorate*/,
                                country = country,
                                address = address,
                                name = contactPerson,
                                phone = "+20 $phoneNumber"
                            )
                        )
                        viewModel.updateCustomerAddress(customerId, addressId, request)
                        backAction()
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    LaunchedEffect(Unit) {
        viewModel.getCustomerAddress(customerId = customerId, addressId = addressId)
    }

    val context = LocalContext.current

    LaunchedEffect(addressLatLon) {
        addressLatLon?.let {
            val (fullAddress, detectedCountry) = MapUtils.getAddressFromLatLng(context, it.latitude, it.longitude)
            country = detectedCountry
            isOutOfCoverage = !detectedCountry.equals("Egypt", ignoreCase = true)
            if (isOutOfCoverage) {
                address = ""
            } else {
                address = fullAddress
            }
        }
    }


    when (val result = customerAddress) {
        is ResponseStatus.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Failed to load address: ${result.error.message}")
            }
        }

        ResponseStatus.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }

        is ResponseStatus.Success -> {
            val fetchedAddress = result.result.customerAddress

            LaunchedEffect(fetchedAddress) {
                title = fetchedAddress.title
                /*selectedGovernorate = fetchedAddress.city*/
                address = fetchedAddress.address
                contactPerson = fetchedAddress.name
                phoneNumber = fetchedAddress.phone.removePrefix("+20 ")
                country = fetchedAddress.country
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_address),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { backAction() }) {
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
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = stringResource(R.string.title)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = validationState.titleError
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = { Text(text = stringResource(R.string.country)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = true,
                    enabled = false
                )

              //  Spacer(modifier = Modifier.height(16.dp))

                /*GovernorateDropdown(
                    selectedGovernorate = selectedGovernorate,
                    onGovernorateSelected = { selected ->
                        selectedGovernorate = selected.name.lowercase().replaceFirstChar { it.uppercaseChar() }
                    }
                )
                if (validationState.governorateError) {
                    Text(
                        text = "Governorate is required",
                        color = Color.Red,
                        style = LocalTextStyle.current.copy(fontSize = 12.sp)
                    )
                }*/

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(text = stringResource(R.string.address)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    readOnly = true,
                    isError = validationState.addressError,
                    trailingIcon = {
                        IconButton(onClick = { navigateToMapAction() }) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Choose on Map",
                                tint = myPurple
                            )
                        }
                    }
                )
                if (isOutOfCoverage) {
                    Text(
                        text = stringResource(R.string.this_address_is_out_of_our_coverage_area),
                        color = Color.Red,
                        style = LocalTextStyle.current.copy(fontSize = 12.sp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = contactPerson,
                    onValueChange = { newValue ->
                        contactPerson = newValue
                            .filter { it.isLetter() || it.isWhitespace() }
                            .split(" ")
                            .joinToString(" ") { word ->
                                word.lowercase().replaceFirstChar { it.uppercaseChar() }
                            }
                    },
                    label = { Text(text = stringResource(R.string.contact_person)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = validationState.contactPersonError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { newValue ->
                        phoneNumber = newValue.filter { it.isDigit() }
                    },
                    label = { Text(text = stringResource(R.string.phone_number)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = validationState.phoneError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text(text = "+20 ", style = LocalTextStyle.current) }
                )

                if (validationState.phoneError) {
                    Text(
                        text = stringResource(R.string.enter_a_valid_egyptian_phone_number),
                        color = Color.Red,
                        style = LocalTextStyle.current.copy(fontSize = 12.sp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                CustomButton(
                    lable = stringResource(R.string.save_changes)
                ) {
                    val isFormValid = viewModel.validateAddressForm(
                        title = title,
                       /* selectedGovernorate = selectedGovernorate,*/
                        address = address,
                        contactPerson = contactPerson,
                        phoneNumber = phoneNumber
                    )

                    if (isFormValid) {

                            showDialog = true
                        }

                    }
                }
            }
        }
    }





