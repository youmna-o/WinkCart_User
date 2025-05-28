package com.example.winkcart_user.ui.utils.navigation


 sealed class NavigationRout(val route: String) {
     object  Login : NavigationRout("Login")
     object  SignUp : NavigationRout("SignUp")
     object Home : NavigationRout("home")
     object Settings : NavigationRout("settings")
     object Profile : NavigationRout("profile")
}

