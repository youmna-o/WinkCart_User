package com.example.winkcart_user.auth.login


import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.winkcart_user.BuildConfig
import com.example.winkcart_user.R
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.auth.AuthViewModel
import com.example.winkcart_user.ui.utils.components.CustomTextField
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel, cartViewModel: CartViewModel) {
    val emailError by authViewModel.signInEmailError.collectAsState()
    val passwordError by authViewModel.signInEmailError.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity

    var shouldNavigate by remember { mutableStateOf(false) }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.FIREBASE_PROJECT_TOKEN)
        .requestEmail()
        .build()


    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                authViewModel.signInWithGoogle(idToken) { success ->
                    if (success) {
                        val email = account.email?.trim() ?: ""

                        Firebase.firestore.collection("customers").document(email)
                            .get()
                            .addOnSuccessListener { document ->
                                val customerId = document.getString("customerId") ?: ""
                                Log.d("shared", "Google Customer ID: $customerId")

                                cartViewModel.writeCustomerID(customerId)

                                shouldNavigate = true
                            }
                            .addOnFailureListener {
                                Log.e("shared", "Firestore fetch failed for Google login", it)
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Failed sign in with Google", e)
        }
    }


    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 106.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Login To WinkCart", style = MaterialTheme.typography.titleLarge)
            TextButton({
                cartViewModel.writeCustomerID("")
                cartViewModel.readCustomerID()
                navController.navigate("home")
            }) {
                Text("Skip", style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(56.dp))

        CustomTextField("Email", email, { email = it }, emailError != "")
        emailError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        CustomTextField("Password", password, { password = it }, passwordError != "", isPassword = true )
        passwordError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Text("Create New Account", modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("SignUp") },
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            authViewModel.signIn(email, password) { success ->
                if (success) {
                    val userEmail = email.trim()
                    Firebase.firestore.collection("customers").document(userEmail)
                        .get()
                        .addOnSuccessListener { document ->
                            val customerId = document.getString("customerId") ?: ""
                            Log.d("shared", "Customer ID: $customerId")
                            cartViewModel.writeCustomerID(customerId)
                            shouldNavigate = true
                        }
                        .addOnFailureListener {
                            Log.e("shared", "Firestore fetch failed", it)
                        }
                } else {
                    Toast.makeText(context, "Wrong Email or Password", Toast.LENGTH_LONG).show()
                }
            }
        }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("LOGIN")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google Icon")
            Spacer(modifier = Modifier.width(8.dp))
            Text("LOGIN WITH GOOGLE")
        }
    }

    if (shouldNavigate) {
        navController.navigate("home") {
            popUpTo("login") { inclusive = true }
        }
    }

}

