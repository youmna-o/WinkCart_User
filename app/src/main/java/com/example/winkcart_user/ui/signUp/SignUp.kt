package com.example.winkcart_user.ui.signUp

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.R
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.FirebaseRepoImp
import com.example.winkcart_user.ui.login.AuthViewModel
import com.example.winkcart_user.ui.utils.CustomTextField
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SignUpScreen(navController: NavController){
    val authViewModel = AuthViewModel(FirebaseRepoImp(RemoteDataSourceImpl(RetrofitHelper())))
    var isInputError = false
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
            Text("Skip", style = MaterialTheme.typography.labelSmall)
        }
        //Text("Sign UP To WinkCart", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 106.dp))
        Spacer(modifier = Modifier.height(56.dp))
        CustomTextField(lable = "Name",input = name,onValueChange = { newName ->
            name = newName
        },isInputError)
        CustomTextField(lable = "Email",input = email,onValueChange = { newEmail ->
            email = newEmail
        },isInputError)
        CustomTextField(lable = "Password",input = password,onValueChange = { newPassword ->
            password = newPassword
        },isInputError)
        Text("Already have an account?LOGIN", modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                navController.navigate("Login")

            }), textAlign = TextAlign.End)


        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                //  authViewModel.signUp(email,password)
                var result = authViewModel.isEmailValid(email)
                Log.i("SignUpScreen", "SignUpScreen: ${result}")
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



