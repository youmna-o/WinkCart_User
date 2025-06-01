package com.example.winkcart_user

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.winkcart_user.ui.login.LoginScreen
import com.example.winkcart_user.ui.login.SignUpScreen

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider

import com.example.winkcart_user.data.local.LocalDataSourceImpl
import com.example.winkcart_user.data.local.settings.SettingsDaoImpl
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.settings.SettingsView
import com.example.winkcart_user.settings.viewmodel.SettingsFactory
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel

import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.utils.navigation.NavigationRout


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            WinkCart_UserTheme {


                val  navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavigationRout.Settings.rout

                ) {


                    composable(NavigationRout.SignUp.rout) { SignUpScreen(navController) }
                    composable(NavigationRout.Login.rout) { LoginScreen(navController)}
                    composable(NavigationRout.Settings.rout) { SettingsView(ViewModelProvider(
                        this@MainActivity,
                        SettingsFactory(
                            ProductRepoImpl(
                                RemoteDataSourceImpl(RetrofitHelper()),
                                LocalDataSourceImpl(SettingsDaoImpl(LocalContext.current.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)))
                            )
                        )
                    )[SettingsViewModel::class.java]
                    ) }


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
           // var state = BrandsViewModel(ProductRepoImpl( RemoteDataSourceImpl(RetrofitHelper()))).brandList.collectAsState()

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