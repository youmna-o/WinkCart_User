package com.example.winkcart_user


import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Modifier
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
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.winkcart_user.ui.home.main.brandsViewModel.BrandsFactory
import com.example.winkcart_user.ui.home.main.brandsViewModel.BrandsViewModel
import com.example.winkcart_user.cart.view.CartView
import com.example.winkcart_user.cart.viewModel.CartFactory
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.ui.categorie.categoriesViewModel.CategoriesViewModel
import com.example.winkcart_user.ui.categorie.categoriesViewModel.CategoryFactory
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
import com.example.winkcart_user.ui.home.main.view.HomeScreen
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorsProductFactory
import com.example.winkcart_user.ui.home.vendorProducts.views.VendorProductScreen
import com.example.winkcart_user.ui.profile.orders.view.OrderDetailsScreen
import com.example.winkcart_user.ui.profile.orders.view.OrdersScreen
import com.example.winkcart_user.ui.profile.orders.view.OrdersScreenOnSucc
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersFactory
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersViewModel
import com.example.winkcart_user.ui.profile.userProfile.view.ProfileScreen

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {


            var authFactory = AuthFactory(FirebaseRepoImp(RemoteDataSourceImpl(RetrofitHelper)))
            var authViewModel = ViewModelProvider(this,authFactory).get(AuthViewModel :: class.java)

            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsFactory(
                    ProductRepoImpl(
                        RemoteDataSourceImpl(RetrofitHelper),
                        LocalDataSourceImpl(
                            SettingsDaoImpl(
                                LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                            )
                        )
                    )
                )
            )


            val cartViewModel: CartViewModel = viewModel(
                factory = CartFactory(
                    repo = ProductRepoImpl(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                        localDataSource =   LocalDataSourceImpl(
                            SettingsDaoImpl(
                                LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                            )
                        )
                    )
                )
            )
            

            val brandFactory = BrandsFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val brandViewModel =  ViewModelProvider(this,brandFactory).get(BrandsViewModel :: class.java)

            val vendorFactory = VendorsProductFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val vendorProductsViewModel =  ViewModelProvider(this,vendorFactory).get(VendorProductsViewModel :: class.java)

             val categoryFactory = CategoryFactory(

                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val categoriesViewModel =  ViewModelProvider(this,categoryFactory).get(
                CategoriesViewModel :: class.java)

            val currencyFactory = CurrencyFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val currencyViewModel =  ViewModelProvider(this,currencyFactory).get(CurrencyViewModel :: class.java)



            val ordersFactory = OrdersFactory(
                repo = ProductRepoImpl(
                    remoteDataSource = RemoteDataSourceImpl(RetrofitHelper) ,
                    localDataSource =   LocalDataSourceImpl(
                        SettingsDaoImpl(
                            LocalContext.current.getSharedPreferences("AppSettings", MODE_PRIVATE)
                        )
                    )
                )
            )
            val ordersViewModel = ViewModelProvider(this,ordersFactory).get(OrdersViewModel::class.java)



            WinkCart_UserTheme {

                AppInit(
                    authViewModel, settingsViewModel = settingsViewModel,
                    cartViewModel = cartViewModel,
                    vendorProductViewModel = vendorProductsViewModel,
                    brandsViewModel = brandViewModel,
                    categoriesViewModel = categoriesViewModel,
                    currencyViewModel = currencyViewModel,
                    ordersViewModel = ordersViewModel,
                )
           }

            }
        }
    }

@Composable
fun AppInit(authViewModel : AuthViewModel,
            cartViewModel: CartViewModel,
            categoriesViewModel : CategoriesViewModel,
            settingsViewModel: SettingsViewModel,
            vendorProductViewModel :VendorProductsViewModel,
            brandsViewModel: BrandsViewModel,
            currencyViewModel : CurrencyViewModel,
            ordersViewModel: OrdersViewModel
) {
    val scroll = rememberScrollState()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val screensWithBottomBar = listOf(
        NavigationRout.Home.route,
        NavigationRout.Cart.route,
        NavigationRout.Settings.route,
        NavigationRout.categories.route,
        NavigationRout.Profile.route
    )
    val showBottomBar = currentRoute in screensWithBottomBar
    LaunchedEffect(currentRoute) {
        Log.i("BottomBar", "Current Route: $currentRoute | Show BottomBar: $showBottomBar")
    }

    WinkCart_UserTheme{
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background ,
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController = navController)
                }
            }
        ) { paddingValues ->
            val pa =paddingValues
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
                composable(NavigationRout.Orders.route) {
                    OrdersScreen(navController = navController, ordersViewModel = ordersViewModel)
                }
                composable(NavigationRout.categories.route) {
                    CategoriesScreen(categoriesViewModel,navController,currencyViewModel)
                }

                composable(NavigationRout.Settings.route) { SettingsView(settingsViewModel) }
                composable(NavigationRout.Cart.route) { CartView(cartViewModel) }
                composable(NavigationRout.Profile.route) { ProfileScreen(navController) }
                composable("vendor_products/{brand}") { backStackEntry ->
                    val brand = backStackEntry.arguments?.getString("brand") ?: ""
                    VendorProductScreen(brand, navController = navController, vendorProductsViewModel = vendorProductViewModel)
                }
                composable(NavigationRout.OrderDetails.route) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                   OrderDetailsScreen(navController,ordersViewModel,orderId.toLong())
                }
                composable(NavigationRout.ProductInfo.route) {
                        backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductInfo(
                        productId.toLong(),
                        navController = navController,
                        scrollState = scroll,
                        categoriesViewModel = categoriesViewModel,
                        cartViewModel = cartViewModel

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
            icon = {   Icon(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "category",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            ) },
            label = { Text("category") },
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
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") },
            selected = currentRoute == NavigationRout.Cart.route,
            onClick = {
                navController.navigate(NavigationRout.Cart.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = {  Icon(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "profile",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            },
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

