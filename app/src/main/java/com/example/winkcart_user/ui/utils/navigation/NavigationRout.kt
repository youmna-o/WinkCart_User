package com.example.winkcart_user.ui.utils.navigation


 sealed class NavigationRout(val route: String) {
     object  Login : NavigationRout("Login")
     object  SignUp : NavigationRout("SignUp")
     data object Settings :NavigationRout("Settings")
     data object Cart :NavigationRout("Cart")
     object Home : NavigationRout("home")
     data object Orders : NavigationRout("orders")
     data object Profile : NavigationRout("Profile")
     data object OrderDetails : NavigationRout("OrderDetails/{orderId}")

     object VendorProducts : NavigationRout("vendor_products/{vendorName}") {
         fun createRoute(vendorName: String) = "vendor_products/$vendorName"
     }

    object  ProductInfo : NavigationRout("ProductInfo/{productId}"){
    fun createRoute(productId: String) = "productId/$productId"
    }

     object categories :NavigationRout("Categories")
     object Favourite :NavigationRout("Favourite")
}

