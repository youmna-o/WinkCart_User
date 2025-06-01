package com.example.winkcart_user.ui.utils.navigation


 sealed class NavigationRout(val route: String) {
     object  Login : NavigationRout("Login")
     object  SignUp : NavigationRout("SignUp")
     data object Settings :NavigationRout("Settings")


     object Home : NavigationRout("home")
     object Settings : NavigationRout("settings")
     object Profile : NavigationRout("profile")
     object VendorProducts : NavigationRout("vendor_products/{vendorName}") {
         fun createRoute(vendorName: String) = "vendor_products/$vendorName"
     }
object  ProductInfo : NavigationRout("ProductInfo/{productId}"){
    fun createRoute(productId: String) = "productId/$productId"
}



}

