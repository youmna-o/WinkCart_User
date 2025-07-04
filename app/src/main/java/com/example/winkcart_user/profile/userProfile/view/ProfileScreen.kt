package com.example.winkcart_user.profile.userProfile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.winkcart_user.auth.AuthViewModel
import com.example.winkcart_user.theme.BackgroundColor
import com.example.winkcart_user.ui.utils.extractUsername

@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel= hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val showDialog = remember { mutableStateOf(false) }
    val showDialogLogout = remember { mutableStateOf(false) }
    val email = remember { profileViewModel.getGemail() }
    val isGuest = remember { profileViewModel.isGuest() }

    Scaffold (    topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "My Profile",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            ),

            )
    },
    containerColor = Color(0xFFF5F5F5)) {
    padding ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(padding)
        ) {
     LazyColumn(
         modifier = Modifier
             .fillMaxSize()
             .background(Color.White)
             .padding(32.dp)

     ) {
         item {
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(bottom = 32.dp),
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 Box(
                     contentAlignment = Alignment.Center,
                     modifier = Modifier
                         .size(64.dp)
                         .clip(CircleShape)
                         .background(Color.Gray.copy(alpha = 0.3f))
                 ) {
                     Text(
                         text = extractUsername(profileViewModel.getGemail())
                             .getOrNull(0)?.toString() ?: "1",
                         fontSize = 24.sp,
                         fontWeight = FontWeight.Bold,
                         color = Color.Gray
                     )
                 }

                 Spacer(modifier = Modifier.width(16.dp))

                 Column {
                     Text(
                         text = if (profileViewModel.isGuest())
                             "Guest"
                         else
                             extractUsername(profileViewModel.getGemail()),
                         fontSize = 18.sp,
                         fontWeight = FontWeight.Medium,
                         color = Color.Black
                     )
                     Text(
                         text = if (isGuest) "" else email,
                         fontSize = 14.sp,
                         color = Color.Gray,
                         modifier = Modifier.padding(top = 4.dp)
                     )
                 }
             }
         }

         item {
             ProfileMenuItem(
                 title = "My Orders",
                 subtitle = "see your delivered orders",
                 onClick = {
                     if (profileViewModel.isGuest()) {
                         showDialog.value = true
                     } else {
                         navController.navigate("orders")
                         val email = profileViewModel.getGemail()

                     }
                 }
             )


             ProfileMenuItem(
                 title = "Settings",
                 subtitle = "addresses,about us etc ",
                 onClick = { navController.navigate("Settings") }
             )

             ProfileMenuItem(
                 title = "Log out",
                 subtitle = "",
                 onClick = {
                     showDialogLogout.value = true

                 }
             )

         }


     }
 }
        if (showDialog.value) {
            CustomAlertDialog(
                onClick = {
                    showDialog.value = false
                    navController.navigate("login")
                },
                onDismissClick = { showDialog.value = false}
            )
        }

        if (showDialogLogout.value) {
            AlertDialog(
                onDismissRequest = { showDialogLogout.value = false },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to logout?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            authViewModel.signOut()
                            profileViewModel.writeCustomerID("")

                            navController.navigate("Login") {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialogLogout.value = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }





}

@Composable
fun ProfileMenuItem(
    title: String,
    subtitle: String,
    onClick:  () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick.invoke() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Navigate",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
@Composable
fun CustomAlertDialog(onClick: () -> Unit,onDismissClick: () -> Unit){
    AlertDialog(
        onDismissRequest = {  },
        title = { Text("Guest Mode") },
        text = { Text("You can't see your orders, You must login first") },
        confirmButton = {
            TextButton(onClick = onClick) {
                Text("Login")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismissClick.invoke()
            }) {
                Text("OK")
            }
        }
    )
}