package com.example.winkcart_user.ui.auth.login

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.winkcart_user.R
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.utils.CustomTextField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private val db = Firebase.firestore
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel, cartViewModel: CartViewModel){
    var context = LocalContext.current
    val emailError by authViewModel.emailError
    val passwordError by authViewModel.passwordError

    var isInputError = false
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 106.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            ){ Text("Login To WinkCart", style = MaterialTheme.typography.titleLarge)
            TextButton({
                cartViewModel.writeCustomerID(null)
                cartViewModel.readCustomerID()
                navController.navigate("home")})
            { Text( "Skip", style = MaterialTheme.typography.labelSmall)}

        }
         Spacer(modifier = Modifier.height(56.dp))
         CustomTextField(lable = "Email", input = email,onValueChange = { newEmail ->
             email = newEmail
         },emailError != null)
        if (emailError != null) {
            Text(emailError ?: "", color = Color.Red, fontSize = 12.sp)
        }
         CustomTextField(lable = "Password",input = password,onValueChange = { newPassword ->
             password = newPassword
         },passwordError != null)
        if (passwordError != null) {
            Text(passwordError ?: "", color = Color.Red, fontSize = 12.sp)
        }
        Text("Create New Account", modifier = Modifier.fillMaxWidth().clickable(
            onClick = {
                navController.navigate("SignUp")
            }
        ), textAlign = TextAlign.End)

        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            authViewModel.signIn(email, password){success ->
                if (success) {
                    val userEmail = email.trim()

                    val docRef = db.collection("customers").document(userEmail)
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                val customerId = document.getString("customerId") ?: ""
                                Log.d("shared", "Customer ID from Firestore: $customerId")
                                Log.d("shared", "************ before auth")
                                cartViewModel.readCustomerID()
                                Log.d("shared", "DocumentSnapshot data: ${document.data}")
                                cartViewModel.writeCustomerID("${customerId}")
                                navController.navigate("home")
                                cartViewModel.readCustomerID()
                                Log.d("shared", "************ after auth")
                            } else {
                                Log.d("shared", "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("shared", "get failed with ", exception)
                        }
                } else {
                    val errorMessage = null
                    Toast.makeText(context, errorMessage ?: "You Have Entered Wrong Email or Password", Toast.LENGTH_LONG).show()
                }
            }

        }, modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),) {
            Text("LOGIN")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "My Image"
            )
            Text("LOGIN WITH GOOGLE")
        }
        Spacer(modifier = Modifier.height(16.dp))



    }

}

