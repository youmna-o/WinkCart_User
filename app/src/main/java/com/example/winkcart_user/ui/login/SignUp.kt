package com.example.winkcart_user.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.winkcart_user.R
import com.example.winkcart_user.ui.utils.CustomTextField
import com.example.winkcart_user.ui.utils.navigation.NavigationRout

@Composable
fun SignUpScreen(navController: NavController){

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text("Sign UP To WinkCart", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 106.dp))
        Spacer(modifier = Modifier.height(56.dp))
        CustomTextField(lable = "Name")
        CustomTextField(lable = "Email")
        CustomTextField(lable = "Password")
        Text("Already have an account?LOGIN", modifier = Modifier.fillMaxWidth()
            .clickable(onClick = {
               navController.navigate("Login")

        }), textAlign = TextAlign.End)


        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp),) {
            Text("SIGN UP")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp),) {
             Image(painter = painterResource(id = R.drawable.google), contentDescription = "")
            Text("SIGN UP WITH GOOGLE")
        }


    }

}


