package com.example.winkcart_user.ui.utils.navigation

sealed class NavigationRout(val route: String) {

     object  Login : NavigationRout("Login")
     object  SignUp : NavigationRout("SignUp")
     data object Settings :NavigationRout("Settings")
     data object Cart :NavigationRout("Cart")
     data object Orders : NavigationRout("orders")
     data object Profile : NavigationRout("Profile")
     data object Checkout : NavigationRout("Checkout")
     data object OrderDetails : NavigationRout("OrderDetails/{orderId}")
    data object Address :NavigationRout("Address")
    data object AddAddress :NavigationRout("AddAddress")
    data object EditAddress : NavigationRout("EditAddress/{customerId}/{addressId}") {
        fun createRoute(customerId: Long, addressId: Long) = "EditAddress/$customerId/$addressId"
    }
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
     object Favourite :NavigationRout("Favourite")
}

