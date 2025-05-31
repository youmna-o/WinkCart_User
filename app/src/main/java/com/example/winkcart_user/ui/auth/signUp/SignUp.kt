package com.example.winkcart_user.ui.auth.signUp

import android.annotation.SuppressLint
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
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.utils.CustomTextField


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SignUpScreen(navController: NavController ,authViewModel: AuthViewModel){
    val emailError by authViewModel.emailError
    val passwordError by authViewModel.passwordError
    var context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 106.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){ Text("Sign UP To WinkCart", style = MaterialTheme.typography.titleLarge)
            TextButton({navController.navigate("home")}) { Text( "Skip", style = MaterialTheme.typography.labelSmall)}
        }
        //Text("Sign UP To WinkCart", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 106.dp))
        Spacer(modifier = Modifier.height(56.dp))
        CustomTextField(lable = "Name",input = name,onValueChange = { newName ->
            name = newName
        },false)
        CustomTextField(lable = "Email",input = email,onValueChange = { newEmail ->
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
        Text("Already have an account?LOGIN", modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                navController.navigate("Login")

            }), textAlign = TextAlign.End)


        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                authViewModel.signUp(email,password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text("SIGN UP")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.signIn(email,password)

        }, modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),) {
            Image(painter = painterResource(id = R.drawable.google), contentDescription = "")
            Text("SIGN UP WITH GOOGLE")
        }
    }
}


