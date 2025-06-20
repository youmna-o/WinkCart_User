package com.example.winkcart_user.payment.view

import android.app.DatePickerDialog
import android.util.Log
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.winkcart_user.R
import com.example.winkcart_user.payment.enums.PaymentMethod
import com.example.winkcart_user.payment.view.components.PaymentOptionCard
import com.example.winkcart_user.payment.view.components.PaymentSectionHeader
import com.example.winkcart_user.payment.viewModel.PaymentViewModel
import com.example.winkcart_user.theme.BackgroundColor
import com.example.winkcart_user.ui.utils.components.TimedLottieAnimation
import com.example.winkcart_user.ui.utils.components.CustomButton
import com.example.winkcart_user.ui.utils.components.PlayOnceThenHideAnimation
import com.example.winkcart_user.ui.utils.Constants.SCREEN_PADDING
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsView(
    viewModel : PaymentViewModel= hiltViewModel(),
    backAction: () -> Unit,
    totalAmount : String,
    currencyCode : String,
    goToCheckout: (String, String) -> Unit,
    couponCode: String
) {
    Log.i("TAG", "PaymentMethodsView: totalAmount: $totalAmount, currencyCode : $currencyCode , couponCode: $couponCode")
    val selectedOption = remember { mutableStateOf<PaymentMethod?>(null) }
    var showCardSheet = remember { mutableStateOf(false) }
    var cardNumber by remember { mutableStateOf("") }
    var isCashAllowed by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf(0.0) }
    var showLottieCheckVerify by remember { mutableStateOf(false) }
    var showLottieVerified by remember { mutableStateOf(false) }


    LaunchedEffect(totalAmount, currencyCode) {
        val cleanedAmount = totalAmount
            .replace(",", "")
            .replace(Regex("[^\\d.]"), "")

        amount = cleanedAmount.toDouble()

        isCashAllowed = when (currencyCode.uppercase()) {
            "USD" -> amount <= 200.0
            "EGP" -> amount <= 1000.0
            else -> true
        }

        Log.d("PaymentMethodsView", "Parsed amount: $amount, isCashAllowed: $isCashAllowed")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.payment_methods),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { backAction.invoke() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
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

                    PaymentSectionHeader(title = stringResource(R.string.online_payment))

                    PaymentOptionCard(
                        title = stringResource(R.string.masterCard),
                        isSelected = selectedOption.value == PaymentMethod.MasterCard,
                        onClick = {
                            selectedOption.value = PaymentMethod.MasterCard
                            showCardSheet.value = true
                        },
                        icon = R.drawable.ic_mastercard
                    )
                    if (isCashAllowed) {
                        PaymentSectionHeader(title = stringResource(R.string.more_payment_options))

                        PaymentOptionCard(
                            title = stringResource(R.string.cash_on_delivery),
                            isSelected = selectedOption.value == PaymentMethod.COD,
                            onClick =
                                {
                                    selectedOption.value = PaymentMethod.COD
                                    cardNumber = "Cash on Delivery (COD)"
                                    goToCheckout(cardNumber, amount.toString())
                                },
                            icon = R.drawable.ic_cod
                        )
                    }
                }
            }
        }

        if (showCardSheet.value) {

            val validationState by viewModel.formValidationState.collectAsState()

            var nameOnCard by remember { mutableStateOf("") }
            var expireDate by remember { mutableStateOf("") }
            var cvv by remember { mutableStateOf("") }


            ModalBottomSheet(
                onDismissRequest = { showCardSheet.value = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.enter_card_details), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nameOnCard,
                        onValueChange = {
                            nameOnCard = it.uppercase()
                        },
                        label = { Text(text = stringResource(R.string.name_on_card)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        isError = validationState.nameOnCartError
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { newValue ->
                            cardNumber = newValue.filter { it.isDigit() }
                        },
                        label = { Text(text = stringResource(R.string.card_number)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = validationState.cardNumberError
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val context = LocalContext.current
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    val dateFormatter = SimpleDateFormat("MM/yy", Locale.getDefault())

                    val datePickerDialog = remember {
                        DatePickerDialog(
                            context,
                            { _, selectedYear, selectedMonth, _ ->
                                val pickedCalendar = Calendar.getInstance()
                                pickedCalendar.set(selectedYear, selectedMonth, 1)
                                expireDate = dateFormatter.format(pickedCalendar.time)
                            },
                            year, month, day
                        ).apply {
                            // Restrict to today or future dates only
                            datePicker.minDate = calendar.timeInMillis

                        }
                    }
                    OutlinedTextField(
                        value = expireDate,
                        onValueChange = { },
                        label = { Text(text = stringResource(R.string.expire_date)) },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        singleLine = true,
                        isError = validationState.expireDateError,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Pick Date"
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { newValue ->
                            cvv = newValue.filter { it.isDigit() }
                        },
                        label = { Text(text = stringResource(R.string.cvv)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = validationState.cvvError
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomButton(lable = stringResource(R.string.apply)) {
                        val isFormValid = viewModel.validateCardForm(
                            nameOnCard = nameOnCard,
                            cardNumber = cardNumber,
                            expireDate = expireDate,
                            cvv = cvv
                        )
                        if (isFormValid) {
                            showLottieCheckVerify = true
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        if (showLottieCheckVerify) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                TimedLottieAnimation(
                    resId = R.raw.animation_loading,
                    durationMillis = 3000L,
                    message = "Verifying your card..."
                )
            }
            LaunchedEffect(showLottieCheckVerify) {
                if (showLottieCheckVerify) {
                    showCardSheet.value = false
                    delay(3000)
                    showLottieCheckVerify = false
                    showLottieVerified = true
                }
            }
        }

        if (showLottieVerified) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                PlayOnceThenHideAnimation(resId = R.raw.verified_animation , message = "Verified!"){
                    showLottieVerified = false
                    goToCheckout("**** **** **** ${cardNumber.takeLast(4)}", amount.toString())
                }

            }

        }
    }
}

