package com.example.winkcart_user

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.winkcart_user.ui.login.LoginScreen
import com.example.winkcart_user.ui.login.SignUpScreen

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.winkcart_user.brands.viewModel.BrandsViewModel
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.ProductRepoImpl

import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.utils.navigation.NavigationRout


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            WinkCart_UserTheme {
                val  navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavigationRout.SignUp.rout

                ) {
                    composable(NavigationRout.SignUp.rout) { SignUpScreen(navController) }
                    composable(NavigationRout.Login.rout) { LoginScreen(navController)}

                }
               // LoginScreen()
                //SignUpScreen()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
////                    Greeting(
////                        name = "Android",
////                        modifier = Modifier.padding(innerPadding)
////                    )
//                }
            }
            var state = BrandsViewModel(ProductRepoImpl( RemoteDataSourceImpl(RetrofitHelper()))).brandList.collectAsState()

        }
    }
}

@Composable
 fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )



}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WinkCart_UserTheme {
        Greeting("Android")
    }
}