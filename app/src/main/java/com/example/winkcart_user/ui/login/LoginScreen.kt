package com.example.winkcart_user.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column

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
import com.example.winkcart_user.ui.utils.CustomButton
import com.example.winkcart_user.ui.utils.CustomTextField


@Composable
fun LoginScreen(navController: NavController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
         Text("Login To WinkCart", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 106.dp))
        Spacer(modifier = Modifier.height(56.dp))
         CustomTextField(lable = "Email", input = email,onValueChange = { newEmail ->
             email = newEmail
         },)
         CustomTextField(lable = "Password",input = password,onValueChange = { newPassword ->
             password = newPassword
         },)
        Text("Forget your password?", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)


        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp),) {
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
            "SIGN UP",
            onClick ={
                navController.navigate("SignUp")
            },
        )
//        Button(onClick = {
//            navController.navigate("SignUp")
//        }, modifier = Modifier.fillMaxWidth().height(48.dp),) {
//            Text("SIGN UP")
//        }


    }

}


