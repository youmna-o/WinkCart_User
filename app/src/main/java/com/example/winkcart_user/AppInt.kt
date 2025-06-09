package com.example.winkcart_user

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.winkcart_user.brands.viewModel.BrandsViewModel
import com.example.winkcart_user.cart.view.CartView
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.categories.viewModel.CategoriesViewModel
import com.example.winkcart_user.favourite.Favourite
import com.example.winkcart_user.favourite.FavouriteViewModel
import com.example.winkcart_user.settings.SettingsView
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.auth.login.LoginScreen
import com.example.winkcart_user.ui.auth.signUp.SignUpScreen
import com.example.winkcart_user.ui.categorie.ui.CategoriesScreen
import com.example.winkcart_user.ui.home.main.HomeScreen
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel
import com.example.winkcart_user.ui.home.vendorProducts.views.VendorProductScreen
import com.example.winkcart_user.ui.productInfo.ProductInfo
import com.example.winkcart_user.ui.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.utils.navigation.NavigationRout
import kotlin.collections.contains

@Composable
fun AppInit(authViewModel : AuthViewModel,
            cartViewModel: CartViewModel,
            categoriesViewModel : CategoriesViewModel,
            settingsViewModel: SettingsViewModel,
            vendorProductViewModel :VendorProductsViewModel,
            brandsViewModel: BrandsViewModel ,
            currencyViewModel : CurrencyViewModel,
            favouriteViewModel: FavouriteViewModel
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
        NavigationRout.Favourite.route,
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
            val pa =paddingValues
            NavHost(
                navController = navController,
                startDestination = when{
                    cartViewModel.readCustomerID()==null -> NavigationRout.Login.route
                    cartViewModel.readCustomerID()!=null -> NavigationRout.Home.route
                        else ->NavigationRout.Home.route
                },
                    //if(cartViewModel.readCustomerID()==null)NavigationRout.Login.route,
                modifier = Modifier.padding(2.dp)
            ) {
                composable(NavigationRout.Login.route) {
                    LoginScreen(navController = navController , authViewModel = authViewModel,cartViewModel)
                }
                composable(NavigationRout.SignUp.route) {
                    SignUpScreen(navController = navController,authViewModel=authViewModel, cartViewModel)
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
                composable(NavigationRout.Cart.route) { CartView(cartViewModel) }
                composable(NavigationRout.Favourite.route) { Favourite(favouriteViewModel) }
                composable(NavigationRout.categories.route) { CategoriesScreen(categoriesViewModel,navController,currencyViewModel) }
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