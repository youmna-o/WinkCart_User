package com.example.winkcart_user


import android.content.Context
import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.winkcart_user.ui.auth.login.LoginScreen
import com.example.winkcart_user.ui.auth.signUp.SignUpScreen

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider

import com.example.winkcart_user.data.local.LocalDataSourceImpl
import com.example.winkcart_user.data.local.settings.SettingsDaoImpl

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.winkcart_user.brands.viewModel.BrandsFactory
import com.example.winkcart_user.brands.viewModel.BrandsViewModel
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import com.example.winkcart_user.categories.viewModel.CategoryFactory
import com.example.winkcart_user.data.remote.RemoteDataSourceImpl
import com.example.winkcart_user.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_user.data.repository.FirebaseRepoImp
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.settings.SettingsView
import com.example.winkcart_user.settings.viewmodel.SettingsFactory
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.ui.auth.AuthFactory
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.categorie.ui.CategoriesScreen
import com.example.winkcart_user.ui.productInfo.ProductInfo

import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.utils.navigation.NavigationRout
import com.example.winkcart_user.ui.home.main.HomeScreen
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorsProductFactory
import com.example.winkcart_user.ui.home.vendorProducts.views.VendorProductScreen

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            var authFactory = AuthFactory(FirebaseRepoImp(RemoteDataSourceImpl(RetrofitHelper())))
            var authViewModel = ViewModelProvider(this,authFactory).get(AuthViewModel :: class.java)
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsFactory(
                    ProductRepoImpl(
                        RemoteDataSourceImpl(RetrofitHelper()),
                        LocalDataSourceImpl(
                            SettingsDaoImpl(
                                LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                            )
                        )
                    )
                )
            )
            var brandFactory = BrandsFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper()) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            var brandViewModel =  ViewModelProvider(this,brandFactory).get(BrandsViewModel :: class.java)

            var vendorFactory = VendorsProductFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper()) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            var vendorProductsViewModel =  ViewModelProvider(this,vendorFactory).get(VendorProductsViewModel :: class.java)

             var categoryFactory = CategoryFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper()) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            var categoriesViewModel =  ViewModelProvider(this,categoryFactory).get(CategoriesViewModel :: class.java)

            var currencyFactory = CurrencyFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper()) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            var currencyViewModel =  ViewModelProvider(this,currencyFactory).get(CurrencyViewModel :: class.java)




            WinkCart_UserTheme {

                AppInit(
                    authViewModel, settingsViewModel = settingsViewModel,
                    vendorProductViewModel = vendorProductsViewModel,
                    brandsViewModel = brandViewModel,
                    categoriesViewModel = categoriesViewModel,
                    currencyViewModel = currencyViewModel
                )
           }

            }



        }
    }

@Composable
fun AppInit(authViewModel : AuthViewModel,
            categoriesViewModel : CategoriesViewModel,
            settingsViewModel: SettingsViewModel,
            vendorProductViewModel :VendorProductsViewModel,
            brandsViewModel: BrandsViewModel ,
            currencyViewModel : CurrencyViewModel


) {
    val scroll = rememberScrollState()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val screensWithBottomBar = listOf(
        NavigationRout.Home.route,
        NavigationRout.Profile.route,
        NavigationRout.Settings.route,
        NavigationRout.categories.route
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
                modifier = Modifier.padding(2.dp)
            ) {
                composable(NavigationRout.Login.route) {
                    LoginScreen(navController = navController , authViewModel = authViewModel)
                }
                composable(NavigationRout.SignUp.route) {
                    SignUpScreen(navController = navController,authViewModel=authViewModel)
                }
                composable(NavigationRout.Home.route) {
                    HomeScreen(navController = navController,brandsViewModel=brandsViewModel)
                }
                composable("vendor_products/{brand}") { backStackEntry ->
                    val brand = backStackEntry.arguments?.getString("brand") ?: ""
                    VendorProductScreen(
                        vendor = brand, navController = navController,
                        vendorProductsViewModel = vendorProductViewModel
                    )
                }
                composable(NavigationRout.Settings.route) { SettingsView(settingsViewModel) }
                composable(NavigationRout.categories.route) { CategoriesScreen(categoriesViewModel,navController,currencyViewModel) }


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
            icon = {  Icon(
                painter = painterResource(id = R.drawable.home),
                contentDescription = "category",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            ) },
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
        NavigationBarItem(
            icon = {   Icon(
                painter = painterResource(id = R.drawable.shopping_cart),
                contentDescription = "category",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            ) },
            label = { Text("Settings") },
            selected = currentRoute == NavigationRout.categories.route,
            onClick = {
                navController.navigate(NavigationRout.categories.route) {
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

