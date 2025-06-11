package com.example.winkcart_user.ui.utils.navigation


 sealed class NavigationRout(val route: String) {
     object  Login : NavigationRout("Login")
     object  SignUp : NavigationRout("SignUp")
     data object Settings :NavigationRout("Settings")
     data object Cart :NavigationRout("Cart")
     data object Address :NavigationRout("Address")
     data object AddAddress :NavigationRout("AddAddress")
     data object EditAddress :NavigationRout("EditAddress")
     data object AboutUs :NavigationRout("AboutUs")
     data object ContactUs :NavigationRout("ContactUs")


     object Home : NavigationRout("home")

     object VendorProducts : NavigationRout("vendor_products/{vendorName}") {
         fun createRoute(vendorName: String) = "vendor_products/$vendorName"
     }

    object  ProductInfo : NavigationRout("ProductInfo/{productId}"){
    fun createRoute(productId: String) = "productId/$productId"
    }

     object categories :NavigationRout("Categories")
}

