package com.example.winkcart_user.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.winkcart_user.R
import com.example.winkcart_user.ui.theme.myOrange

@Composable
fun LoginScreen(){
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
         Text("Login To WinkCart", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 106.dp))
        Spacer(modifier = Modifier.height(56.dp))
         CustomTextField(lable = "Email")
         CustomTextField(lable = "Password")
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


    }

}

@Composable
fun CustomTextField(lable: String){
    val myText = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        OutlinedTextField(
            value = myText.value,
            onValueChange = { myText.value = it },
            label = { Text(lable) },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
