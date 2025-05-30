package com.example.winkcart_user

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.winkcart_user.ui.login.LoginScreen
import com.example.winkcart_user.ui.login.SignUpScreen

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.utils.navigation.NavigationRout
import com.example.winkcart_user.ui.home.main.HomeScreen
import com.example.winkcart_user.ui.home.vendorProducts.views.VendorProductScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var vm =  CategoriesViewModel(ProductRepoImpl( RemoteDataSourceImpl(RetrofitHelper())))

        setContent {
            val productState = vm.producs.collectAsState()

         
            var myProducts =  remember (productState.value) {
                vm.getProductsList()
            }
            var myProduct =  remember (productState.value) {
                vm.getProduct(id=9083149353208)
            }
            LaunchedEffect(myProduct) {
    

                Log.i("Product", "**************************: ${myProduct?.body_html}")
                Log.i("Product", "**************************: ${myProduct?.title}")
                myProduct?.images?.forEach { image ->
                    Log.i("Product", "**************************: ${image.src}")
                }
                myProduct?.options?.filter {
                    it.name == "Size"
                }?.forEach { size ->
                     Log.i("Product", "**************************: ${size.values}")
                }
                
                    Log.i("Product", "**************************: ${myProduct?.variants?.get(0)?.price}")
                     Log.i("Product", "------------------: ${vm.getRate()}")
                      Log.i("Product", "------------------: ${vm.getReview()}")
                }



            WinkCart_UserTheme {
                AppInit()
            }
        }
    }
}
@Composable
fun AppInit() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val screensWithBottomBar = listOf(
        NavigationRout.Home.route,
        NavigationRout.Profile.route,
        NavigationRout.Settings.route
    )
    val showBottomBar = currentRoute in screensWithBottomBar


    Scaffold(
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
                LoginScreen(navController = navController)
            }
            composable(NavigationRout.SignUp.route) {
                SignUpScreen(navController = navController)
            }
            composable(NavigationRout.Home.route) {
                HomeScreen(navController = navController)
            }
            composable("vendor_products/{brand}") { backStackEntry ->
                val brand = backStackEntry.arguments?.getString("brand") ?: ""
                VendorProductScreen(brand = brand)
            }
            //    NavHost(
//        navController = navController
//            .startDestination = NavigationRout.SignUp.rout
//
//    ) {
//        composable(NavigationRout.SignUp.rout) { SignUpScreen(navController) }
//        composable(NavigationRout.Login.rout) { LoginScreen(navController)}
//    }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar {
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

