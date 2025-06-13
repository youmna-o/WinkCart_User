package com.example.winkcart_user

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.winkcart_user.cart.view.CartView
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.favourite.Favourite
import com.example.winkcart_user.favourite.FavouriteViewModel
import com.example.winkcart_user.payment.view.PaymentMethodsView
import com.example.winkcart_user.payment.viewModel.PaymentViewModel
import com.example.winkcart_user.settings.view.SettingsView
import com.example.winkcart_user.settings.view.aboutus.AboutUsView
import com.example.winkcart_user.settings.view.address.AddAddressView
import com.example.winkcart_user.settings.view.address.AddressView
import com.example.winkcart_user.settings.view.address.EditAddressView
import com.example.winkcart_user.settings.view.contactus.ContactUsView
import com.example.winkcart_user.settings.viewmodel.SettingsViewModel
import com.example.winkcart_user.ui.auth.AuthViewModel
import com.example.winkcart_user.ui.auth.login.LoginScreen
import com.example.winkcart_user.ui.auth.signUp.SignUpScreen
import com.example.winkcart_user.ui.categorie.categoriesViewModel.CategoriesViewModel
import com.example.winkcart_user.ui.categorie.ui.CategoriesScreen
import com.example.winkcart_user.ui.checkout.view.CheckoutScreen
import com.example.winkcart_user.ui.checkout.view.viewModel.CheckoutViewModel
import com.example.winkcart_user.ui.home.main.brandsViewModel.BrandsViewModel
import com.example.winkcart_user.ui.home.main.view.HomeScreen
import com.example.winkcart_user.ui.home.vendorProducts.viewModel.VendorProductsViewModel
import com.example.winkcart_user.ui.home.vendorProducts.views.VendorProductScreen
import com.example.winkcart_user.ui.productInfo.ProductInfo
import com.example.winkcart_user.ui.profile.orders.view.OrderDetailsScreen
import com.example.winkcart_user.ui.profile.orders.view.OrdersScreen
import com.example.winkcart_user.ui.profile.orders.viewModel.OrdersViewModel
import com.example.winkcart_user.ui.profile.userProfile.view.ProfileScreen
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
            favouriteViewModel: FavouriteViewModel,
            ordersViewModel : OrdersViewModel,
            checkoutViewModel: CheckoutViewModel,
            paymentViewModel: PaymentViewModel
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
        NavigationRout.Profile.route
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
                startDestination =
                when{

                  
                    cartViewModel.readCustomersID().isBlank() -> NavigationRout.SignUp.route
                      else -> NavigationRout.Home.route
                     ///   else ->NavigationRout.Login.route

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
                composable(NavigationRout.Profile.route) { ProfileScreen(navController) }
                composable(NavigationRout.Settings.route) {
                    SettingsView(
                        viewModel = settingsViewModel,
                        addressAction = {navController.navigate(NavigationRout.Address.route)},
                        contactUsAction = {navController.navigate(NavigationRout.ContactUs.route)},
                        aboutUsAction = {navController.navigate(NavigationRout.AboutUs.route)},
                        backAction = {navController.popBackStack()}
                    )
                }
               // composable(NavigationRout.Cart.route) { CartView(cartViewModel,navController) }
                composable(NavigationRout.Cart.route) { CartView(
                    viewModel = cartViewModel,
                    checkoutAction = {navController.navigate(NavigationRout.PaymentMethods.route)}
                ) }
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
                        cartViewModel = cartViewModel,
                      //  favouriteViewModel= favouriteViewModel,

                    )
                }
                composable(NavigationRout.Address.route) {
                    AddressView(
                        viewModel = settingsViewModel,
                        addAction = {navController.navigate(NavigationRout.AddAddress.route)},
                        backAction = {navController.popBackStack()},
                        editAction = { customerId, addressId ->
                            navController.navigate(
                                NavigationRout.EditAddress.createRoute(customerId, addressId)
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set("addressId", addressId)
                        }

                    )
                }

                composable(NavigationRout.AddAddress.route) { AddAddressView(
                    viewModel = settingsViewModel,
                    backAction = {navController.popBackStack()}
                ) }
                composable(NavigationRout.ContactUs.route) { ContactUsView() }
                composable(NavigationRout.AboutUs.route) { AboutUsView() }

                composable(
                    route = NavigationRout.EditAddress.route,
                    arguments = listOf(
                        navArgument("customerId") { type = NavType.LongType },
                        navArgument("addressId") { type = NavType.LongType }
                    )
                ) { backStackEntry ->
                    val customerId = backStackEntry.arguments?.getLong("customerId") ?: return@composable
                    val addressId = backStackEntry.arguments?.getLong("addressId") ?: return@composable

                    EditAddressView(
                        customerId = customerId,
                        addressId = addressId,
                        viewModel = settingsViewModel,
                        backAction = {navController.popBackStack()}
                    )
                }

                composable(NavigationRout.OrderDetails.route) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                    OrderDetailsScreen(navController,ordersViewModel,orderId.toLong())
                }
                composable(NavigationRout.Orders.route) {
                    OrdersScreen(navController = navController, ordersViewModel = ordersViewModel)
                }
                composable(NavigationRout.Checkout.route) {
                    CheckoutScreen(cartViewModel,currencyViewModel,navController,checkoutViewModel)
                }

                composable(NavigationRout.PaymentMethods.route) { PaymentMethodsView(
                    viewModel = paymentViewModel,
                    backAction = {navController.popBackStack()}
                )}
            }
        }
    }

}