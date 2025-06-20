
package com.example.winkcart_user

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.winkcart_user.ui.utils.navigation.NavigationRout

@Composable
fun BottomNavigationBar(navController: NavController, customerId: String) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route


    var showLoginDialog by remember { mutableStateOf(false) }
    var pendingRoute by remember { mutableStateOf<String?>(null) }

    if (showLoginDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("Login Required") },
            text = { Text("You need to login to access this section.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLoginDialog = false
                        navController.navigate("login") {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("Login")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoginDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "category",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Home") },
            selected = currentRoute == NavigationRout.Home.route,
            onClick = {
                navController.navigate(NavigationRout.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Category",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Category") },
            selected = currentRoute == NavigationRout.categories.route,
            onClick = {
                navController.navigate(NavigationRout.categories.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        listOf(
            NavigationRout.Cart.route to R.drawable.cart1,
            NavigationRout.Favourite.route to R.drawable.shape_fav,
            NavigationRout.Profile.route to R.drawable.profile
        ).forEach { (route, iconRes) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = route,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(route.substringAfterLast('.')) },
                selected = currentRoute == route,
                onClick = {
                    if (customerId.isBlank()) {
                        showLoginDialog = true
                        pendingRoute = route
                    } else {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
