package com.example.winkcart_user.ui.utils.navigation

sealed class NavigationRout(val route: String) {

     object  Login : NavigationRout("Login")
     object  SignUp : NavigationRout("SignUp")
     data object Settings :NavigationRout("Settings")
     data object Cart :NavigationRout("Cart")
     data object Orders : NavigationRout("orders")
     data object Profile : NavigationRout("Profile")
     data object Checkout : NavigationRout("Checkout/{cardNumber}/{totalAmount}/{currencyCode}/{couponCode}"){
         fun createRoute(cardNumber: String, totalAmount: String, currencyCode: String, couponCode: String) = "Checkout/$cardNumber/$totalAmount/$currencyCode/$couponCode"
     }
     data object OrderDetails : NavigationRout("OrderDetails/{orderId}")
    data object Address :NavigationRout("Address")
    data object AddAddress :NavigationRout("AddAddress")
    data object EditAddress : NavigationRout("EditAddress/{customerId}/{addressId}") {
        fun createRoute(customerId: Long, addressId: Long) = "EditAddress/$customerId/$addressId"
    }
    data object AboutUs :NavigationRout("AboutUs")
    data object Splash :NavigationRout("Splash")
    data object ContactUs :NavigationRout("ContactUs")
     data object PaymentMethods :NavigationRout("PaymentMethods/{totalAmount}/{currencyCode}/{couponCode}") {
         fun createRoute(totalAmount: String, currencyCode: String, couponCode: String) = "PaymentMethods/$totalAmount/$currencyCode/$couponCode"
     }

     object Home : NavigationRout("home")
     object VendorProducts : NavigationRout("vendor_products/{vendorName}") {
         fun createRoute(vendorName: String) = "vendor_products/$vendorName"
     }

    object  ProductInfo : NavigationRout("ProductInfo/{productId}"){
    fun createRoute(productId: String) = "productId/$productId"
    }

     object categories :NavigationRout("Categories")
     object Favourite :NavigationRout("Favourite")
    data object Map: NavigationRout("map_screen")
    data object Success: NavigationRout("Success")

}

