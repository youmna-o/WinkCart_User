package com.example.winkcart_user.ui.utils.navigation

 sealed class NavigationRout(val rout: String) {
     object  Login : NavigationRout("Login")
     object  SignUp : NavigationRout("SignUp")
     data object Settings :NavigationRout("Settings")
}