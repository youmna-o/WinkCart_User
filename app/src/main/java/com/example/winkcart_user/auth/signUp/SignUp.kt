package com.example.winkcart_user.auth.signUp

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.data.model.customer.CustomerRequest
import com.example.winkcart_user.auth.AuthViewModel
import com.example.winkcart_user.ui.utils.CustomSmallTextField
import com.example.winkcart_user.ui.utils.CustomTextField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private val db = Firebase.firestore
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SignUpScreen(navController: NavController ,authViewModel: AuthViewModel , cartViewModel: CartViewModel){
    val emailError by authViewModel.signUpEmailError.collectAsState()
    val passwordError by authViewModel.signUppasswordError.collectAsState()
    var context = LocalContext.current

    var showVerificationDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    if (showVerificationDialog) {
        AlertDialog(
            onDismissRequest = { showVerificationDialog = false },
            title = { Text(" Email Verification") },
            text = { Text("We've sent you a verification link to your email. Please click the link to finish Registration") },
            confirmButton = {
                Button(onClick = { showVerificationDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 106.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){ Text("Sign UP To WinkCart", style = MaterialTheme.typography.titleLarge)
            TextButton({
                cartViewModel.writeCustomerID("")
                Log.d("shared", "************ after auth")
                cartViewModel.readCustomerID()
                navController.navigate("home")})
            { Text( "Skip", style = MaterialTheme.typography.labelSmall)}
        }
        Spacer(modifier = Modifier.height(56.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            CustomSmallTextField(
                lable = "First Name",
                input = firstName,
                onValueChange = { newName -> firstName = newName },
                isEmailError = false,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            )
            CustomSmallTextField(
                lable = "Last Name",
                input = lastName,
                onValueChange = { newName -> lastName = newName },
                isEmailError = false,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            )
        }

        CustomTextField(lable = "Email",input = email,onValueChange = { newEmail ->
            email = newEmail
        },emailError != "")
        if (emailError != "") {
            Text(emailError ?: "", color = Color.Red, fontSize = 12.sp)
        }
        CustomTextField(lable = "Password",input = password,onValueChange = { newPassword ->
            password = newPassword
        },passwordError != "", isPassword = true)
        if (passwordError != "") {
            Text(passwordError ?: "", color = Color.Red, fontSize = 12.sp)
        }
        Text("Already have an account?LOGIN", modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                navController.navigate("Login")

            }), textAlign = TextAlign.End)

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                authViewModel.signUp(
                    email = email,
                    password = password,
                    onVerificationSent = { verificationSent ->
                        if (verificationSent) {
                            showVerificationDialog = true
                        } else {
                            Toast.makeText(context, "Failed To Send verification", Toast.LENGTH_LONG).show()
                        }
                    },
                    onVerified = { verifiedUser ->
                        verifiedUser?.let { user ->
                            val customerRequest = CustomerRequest(
                                first_name = firstName,
                                last_name = lastName,
                                email = email,
                                password = password,
                                password_confirmation = password
                            )
                            authViewModel.postCustomer(customerRequest) { shopifyId ->
                                if (shopifyId != null) {
                                    val customerMap = hashMapOf("customerId" to shopifyId)
                                    db.collection("customers").document(email.trim()).set(customerMap)
                                        .addOnSuccessListener {
                                            cartViewModel.writeCustomerID(shopifyId)
                                            navController.navigate("home")
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Failed to save ID", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    Toast.makeText(context, "Failed to register", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                )

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("SIGN UP")
        }
        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//
//
//        }, modifier = Modifier
//            .fillMaxWidth()
//            .height(48.dp),) {
//            Image(painter = painterResource(id = R.drawable.google), contentDescription = "")
//            Text("SIGN UP WITH GOOGLE")
//        }
    }
}


