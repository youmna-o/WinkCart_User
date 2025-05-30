package com.example.winkcart_user.ui.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.R
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.FirebaseRepoImp
import com.example.winkcart_user.ui.utils.CustomButton
import com.example.winkcart_user.ui.utils.CustomTextField
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LoginScreen(navController: NavController){
    val authViewModel = AuthViewModel(FirebaseRepoImp(RemoteDataSourceImpl(RetrofitHelper())))

    var isInputError = false
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {

        Row (modifier = Modifier.fillMaxWidth()
            .padding(top = 106.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            ){ Text("Login To WinkCart", style = MaterialTheme.typography.titleLarge)
                Text("Skip", style = MaterialTheme.typography.labelSmall)
        }
         Spacer(modifier = Modifier.height(56.dp))
         CustomTextField(lable = "Email", input = email,onValueChange = { newEmail ->
             email = newEmail
         },isInputError)
         CustomTextField(lable = "Password",input = password,onValueChange = { newPassword ->
             password = newPassword
         },isInputError)
        Text("Forget your password?", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)


        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            authViewModel.signIn(email,password)
        }, modifier = Modifier.fillMaxWidth().height(48.dp),) {
            Text("LOGIN")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp),) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "My Image"
            )
            Text("LOGIN WITH GOOGLE")
        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(
            "Product Info",
            //"SIGN UP",
            onClick ={
                navController.navigate("ProductInfo")
                //   navController.navigate("SignUp")
            },
        )
//        Button(onClick = {
//            navController.navigate("SignUp")
//        }, modifier = Modifier.fillMaxWidth().height(48.dp),) {
//            Text("SIGN UP")
//        }


    }

}

