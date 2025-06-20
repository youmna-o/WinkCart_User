package com.example.winkcart_user

import Splash
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
import com.example.winkcart_user.favourite.view.Favourite
import com.example.winkcart_user.payment.view.PaymentMethodsView
import com.example.winkcart_user.settings.view.SettingsView
import com.example.winkcart_user.settings.view.aboutus.AboutUsView
import com.example.winkcart_user.settings.view.address.AddAddressView
import com.example.winkcart_user.settings.view.address.AddressView
import com.example.winkcart_user.settings.view.address.EditAddressView
import com.example.winkcart_user.settings.view.contactus.ContactUsView
import com.example.winkcart_user.settings.viewmodel.map.PlacesViewModel
import com.example.winkcart_user.settings.view.address.map.PlacePicker
import com.example.winkcart_user.auth.login.LoginScreen
import com.example.winkcart_user.auth.signUp.SignUpScreen
import com.example.winkcart_user.categorie.view.CategoriesScreen
import com.example.winkcart_user.data.repository.ProductRepo
import com.example.winkcart_user.payment.view.CheckoutScreen
import com.example.winkcart_user.payment.view.SuccessView
import com.example.winkcart_user.home.main.view.HomeScreen
import com.example.winkcart_user.home.vendorProducts.views.VendorProductScreen
import com.example.winkcart_user.productInfo.ProductInfo
import com.example.winkcart_user.profile.orders.view.OrderDetailsScreen
import com.example.winkcart_user.profile.orders.view.OrdersScreen
import com.example.winkcart_user.profile.orders.viewModel.OrdersViewModel
import com.example.winkcart_user.profile.userProfile.view.ProfileScreen
import com.example.winkcart_user.theme.WinkCart_UserTheme
import com.example.winkcart_user.ui.utils.NetworkAwareWrapper
import com.example.winkcart_user.ui.utils.navigation.NavigationRout
import com.google.android.gms.maps.model.LatLng
import kotlin.collections.contains

@Composable
fun AppInit(
            cartViewModel: CartViewModel,
            ordersViewModel : OrdersViewModel,
            placesViewModel: PlacesViewModel,
            repo: ProductRepo

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

    WinkCart_UserTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController = navController, customerId = repo.readCustomersID() )
                }
            }
        ) { paddingValues ->
            val pa = paddingValues
            NetworkAwareWrapper {
                NavHost(

                    navController = navController,
                    startDestination = NavigationRout.Splash.route,

                    modifier = Modifier.padding(2.dp)
                ) {
                    var addressLatLon: LatLng? = null
                    composable(NavigationRout.Splash.route) {
                        Splash(navController, cartViewModel)
                    }
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
                        VendorProductScreen(
                            vendor = brand, navController = navController,
                        )
                    }
                    composable(NavigationRout.Profile.route) {
                        ProfileScreen(
                            navController
                        )
                    }
                    composable(NavigationRout.Settings.route) {
                        SettingsView(
                            addressAction = { navController.navigate(NavigationRout.Address.route) },
                            contactUsAction = { navController.navigate(NavigationRout.ContactUs.route) },
                            aboutUsAction = { navController.navigate(NavigationRout.AboutUs.route) },
                            backAction = { navController.popBackStack() }
                        )
                    }
                    composable(NavigationRout.Cart.route) {
                        CartView(
                            checkoutAction = { totalAmount, currencyCode, couponCode, discount ->
                                navController.navigate(
                                    NavigationRout.PaymentMethods.createRoute(
                                        totalAmount,
                                        currencyCode,
                                        couponCode,
                                        discount = discount
                                    )

                                )
                            },
                            backAction = { navController.popBackStack() },
                            navController = navController,
                            viewModel = cartViewModel
                        )
                    }
                    composable(NavigationRout.Favourite.route) { Favourite(navController = navController) }
                    composable(NavigationRout.categories.route) { CategoriesScreen(navController = navController) }
                    composable(NavigationRout.ProductInfo.route) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId") ?: ""
                        ProductInfo(
                            productId.toLong(),
                            navController = navController,
                            scrollState = scroll
                        )
                    }
                    composable(NavigationRout.Address.route) {
                        AddressView(
                            addAction = { navController.navigate(NavigationRout.AddAddress.route) },
                            backAction = { navController.popBackStack() },
                            editAction = { customerId, addressId ->
                                navController.navigate(
                                    NavigationRout.EditAddress.createRoute(customerId, addressId)
                                )
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "addressId",
                                    addressId
                                )
                            }

                        )
                    }

                    composable(NavigationRout.AddAddress.route) {
                        AddAddressView(
                            backAction = { navController.popBackStack() },
                            navigateToMapAction = { navController.navigate(NavigationRout.Map.route) },
                            addressLatLon = addressLatLon
                        )
                    }
                    composable(NavigationRout.ContactUs.route) { ContactUsView(
                        backAction = {navController.popBackStack()}
                    ) }
                    composable(NavigationRout.AboutUs.route) { AboutUsView(
                        backAction = {navController.popBackStack()}
                    ) }

                    composable(
                        route = NavigationRout.EditAddress.route,
                        arguments = listOf(
                            navArgument("customerId") { type = NavType.LongType },
                            navArgument("addressId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val customerId =
                            backStackEntry.arguments?.getLong("customerId") ?: return@composable
                        val addressId =
                            backStackEntry.arguments?.getLong("addressId") ?: return@composable

                        EditAddressView(
                            customerId = customerId,
                            addressId = addressId,
                            backAction = { navController.popBackStack() },
                            navigateToMapAction = { navController.navigate(NavigationRout.Map.route) },
                            addressLatLon = addressLatLon
                        )
                    }

                    composable(NavigationRout.OrderDetails.route) { backStackEntry ->
                        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                        OrderDetailsScreen(navController, ordersViewModel, orderId.toLong())
                    }
                    composable(NavigationRout.Orders.route) {
                        OrdersScreen(navController = navController)
                    }
                    composable(
                        NavigationRout.Checkout.route,
                        arguments = listOf(
                            navArgument("cardNumber") { type = NavType.StringType },
                            navArgument("totalAmount") { type = NavType.StringType },
                            navArgument("currencyCode") { type = NavType.StringType },
                            navArgument("couponCode") { type = NavType.StringType },
                            navArgument("discount") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val cardNumber =
                            backStackEntry.arguments?.getString("cardNumber") ?: return@composable
                        val totalAmount =
                            backStackEntry.arguments?.getString("totalAmount") ?: return@composable
                        val currencyCode =
                            backStackEntry.arguments?.getString("currencyCode") ?: return@composable
                        val couponCode =
                            backStackEntry.arguments?.getString("couponCode") ?: return@composable
                        val discount =
                            backStackEntry.arguments?.getString("discount") ?: return@composable
                        CheckoutScreen(
                            navController = navController,
                            cardNumber = cardNumber,
                            totalAmount = totalAmount,
                            currencyCode = currencyCode,
                            goToSuccess = {
                                navController.navigate(NavigationRout.Success.route)
                            },
                            couponCode = couponCode,
                            discount = discount
                        )

                    }

                    composable(
                        NavigationRout.PaymentMethods.route,
                        arguments = listOf(
                            navArgument("totalAmount") { type = NavType.StringType },
                            navArgument("currencyCode") { type = NavType.StringType },
                            navArgument("couponCode") { type = NavType.StringType },
                            navArgument("discount") { type = NavType.StringType },
                        )
                    ) { backStackEntry ->
                        val totalAmount =
                            backStackEntry.arguments?.getString("totalAmount") ?: return@composable
                        val currencyCode =
                            backStackEntry.arguments?.getString("currencyCode") ?: return@composable
                        val couponCode =
                            backStackEntry.arguments?.getString("couponCode") ?: return@composable
                        val discount =
                            backStackEntry.arguments?.getString("discount") ?: return@composable
                        PaymentMethodsView(
                            backAction = { navController.popBackStack() },
                            totalAmount = totalAmount,
                            currencyCode = currencyCode,
                            goToCheckout = { cardNumber, amount ->
                                navController.navigate(
                                    NavigationRout.Checkout.createRoute(
                                        cardNumber,
                                        amount,
                                        currencyCode,
                                        couponCode,
                                        discount = discount
                                    )

                                )
                            },
                            couponCode = couponCode
                        )
                    }

                    composable(NavigationRout.Success.route) {
                        SuccessView(
                            backAction = {
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            }
                        )
                    }

                    //map

                    composable(NavigationRout.Map.route) {
                        PlacePicker(
                            placesViewModel
                        ) { latLon ->
                            addressLatLon = latLon
                            navController.popBackStack()

                        }

                    }

                    composable(NavigationRout.AddAddress.route) {
                        AddAddressView(
                            backAction = { navController.popBackStack() },
                            navigateToMapAction = { navController.navigate(NavigationRout.Map.route) },
                            addressLatLon = addressLatLon
                        )
                    }
                    composable(NavigationRout.ContactUs.route) { ContactUsView(backAction = { navController.popBackStack() }) }
                    composable(NavigationRout.AboutUs.route) { AboutUsView(backAction = { navController.popBackStack() }) }

                    composable(
                        route = NavigationRout.EditAddress.route,
                        arguments = listOf(
                            navArgument("customerId") { type = NavType.LongType },
                            navArgument("addressId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val customerId =
                            backStackEntry.arguments?.getLong("customerId") ?: return@composable
                        val addressId =
                            backStackEntry.arguments?.getLong("addressId") ?: return@composable

                        EditAddressView(
                            customerId = customerId,
                            addressId = addressId,
                            backAction = { navController.popBackStack() },
                            navigateToMapAction = { navController.navigate(NavigationRout.Map.route) },
                            addressLatLon = addressLatLon
                        )
                    }

                    composable(NavigationRout.OrderDetails.route) { backStackEntry ->
                        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                        OrderDetailsScreen(navController, ordersViewModel, orderId.toLong())
                    }
                    composable(NavigationRout.Orders.route) {
                        OrdersScreen(
                            navController = navController,
                            ordersViewModel = ordersViewModel
                        )
                    }
                    composable(
                        NavigationRout.Checkout.route,
                        arguments = listOf(
                            navArgument("cardNumber") { type = NavType.StringType },
                            navArgument("totalAmount") { type = NavType.StringType },
                            navArgument("currencyCode") { type = NavType.StringType },
                            navArgument("couponCode") { type = NavType.StringType },
                            navArgument("discount") { type = NavType.StringType }

                            )
                    ) { backStackEntry ->
                        val cardNumber =
                            backStackEntry.arguments?.getString("cardNumber") ?: return@composable
                        val totalAmount =
                            backStackEntry.arguments?.getString("totalAmount") ?: return@composable
                        val currencyCode =
                            backStackEntry.arguments?.getString("currencyCode") ?: return@composable
                        val couponCode =
                            backStackEntry.arguments?.getString("couponCode") ?: return@composable
                        val discount =
                            backStackEntry.arguments?.getString("discount") ?: return@composable
                        CheckoutScreen(
                            navController = navController,
                            cardNumber = cardNumber,
                            totalAmount = totalAmount,
                            currencyCode = currencyCode,
                            goToSuccess = {
                                navController.navigate(NavigationRout.Success.route)
                            },
                            couponCode = couponCode,
                            discount = discount
                        )

                    }


                    composable(
                        NavigationRout.PaymentMethods.route,
                        arguments = listOf(
                            navArgument("totalAmount") { type = NavType.StringType },
                            navArgument("currencyCode") { type = NavType.StringType },
                            navArgument("couponCode") { type = NavType.StringType },
                            navArgument("discount") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val totalAmount =
                            backStackEntry.arguments?.getString("totalAmount") ?: return@composable
                        val currencyCode =
                            backStackEntry.arguments?.getString("currencyCode") ?: return@composable
                        val couponCode =
                            backStackEntry.arguments?.getString("couponCode") ?: return@composable
                        val discount =
                            backStackEntry.arguments?.getString("discount") ?: return@composable
                        PaymentMethodsView(
                            backAction = { navController.popBackStack() },
                            totalAmount = totalAmount,
                            currencyCode = currencyCode,
                            goToCheckout = { cardNumber, amount ->
                                navController.navigate(
                                    NavigationRout.Checkout.createRoute(
                                        cardNumber,
                                        amount,
                                        currencyCode,
                                        couponCode = couponCode,
                                        discount = discount
                                    )

                                )
                            },
                            couponCode = couponCode
                        )
                    }

                    composable(NavigationRout.Success.route) {
                        SuccessView(
                            backAction = {
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            }
                        )
                    }

                    //map

                    composable(NavigationRout.Map.route) {
                        PlacePicker(
                            placesViewModel = placesViewModel
                        ) { latLon ->
                            addressLatLon = latLon
                            navController.popBackStack()

                        }
                    }
                }
            }
        }
    }
}

