package com.example.winkcart_user
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.winkcart_user.ui.auth.login.LoginScreen
import com.example.winkcart_user.ui.auth.signUp.SignUpScreen

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.ViewModelProvider
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.FirebaseRepoImp
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.ui.auth.AuthFactory
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.home.ads.ADSPager
import com.example.winkcart_user.ui.productInfo.ProductInfo

import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.utils.navigation.NavigationRout
import com.example.winkcart_user.ui.home.main.HomeScreen
import com.example.winkcart_user.ui.home.vendorProducts.views.VendorProductScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            var authFactory = AuthFactory(FirebaseRepoImp(RemoteDataSourceImpl(RetrofitHelper())))
            var authViewModel = ViewModelProvider(this,authFactory).get(AuthViewModel :: class.java)
            WinkCart_UserTheme {
                //AppInit(authViewModel)
                ADSPager()
           }

            }

           // var state = BrandsViewModel(ProductRepoImpl( RemoteDataSourceImpl(RetrofitHelper()))).brandList.collectAsState()


        }
    }

@Composable
fun AppInit(authViewModel : AuthViewModel, categoriesViewModel : CategoriesViewModel =  CategoriesViewModel(ProductRepoImpl( RemoteDataSourceImpl(RetrofitHelper())))) {
    val scroll = rememberScrollState()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val screensWithBottomBar = listOf(
        NavigationRout.Home.route,
        NavigationRout.Profile.route,
        NavigationRout.Settings.route
    )
    val showBottomBar = currentRoute in screensWithBottomBar

    WinkCart_UserTheme{
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background ,
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController = navController)
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = NavigationRout.Login.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(NavigationRout.Login.route) {
                    LoginScreen(navController = navController , authViewModel = authViewModel)
                }
                composable(NavigationRout.SignUp.route) {
                    SignUpScreen(navController = navController,authViewModel=authViewModel)
                }
                composable(NavigationRout.Home.route) {
                    HomeScreen(navController = navController)
                }
                composable("vendor_products/{brand}") { backStackEntry ->
                    val brand = backStackEntry.arguments?.getString("brand") ?: ""
                    VendorProductScreen(vendor = brand,navController = navController)
                }
                composable(NavigationRout.ProductInfo.route) {
                        backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductInfo(
                        productId.toLong(),
                        navController = navController,
                        scrollState = scroll,
                        viewModel = categoriesViewModel,

                    )
                }
            }
        }
    }

}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == NavigationRout.Home.route,
            onClick = {
                navController.navigate(NavigationRout.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ThumbUp, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == NavigationRout.Profile.route,
            onClick = {
                navController.navigate(NavigationRout.Profile.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == NavigationRout.Settings.route,
            onClick = {
                navController.navigate(NavigationRout.Settings.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

