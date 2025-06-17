import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.winkcart_user.cart.viewModel.CartViewModel
import com.example.winkcart_user.ui.utils.navigation.NavigationRout
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import com.example.winkcart_user.R


import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.example.winkcart_user.ui.theme.Rose40


@Composable
fun Splash(navController: NavController, viewModel: CartViewModel= hiltViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        delay(3000)


        if (viewModel.readCustomersID().isBlank()) {
            navController.navigate(NavigationRout.Login.route) {
                popUpTo(NavigationRout.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(NavigationRout.Home.route) {
                popUpTo(NavigationRout.Splash.route) { inclusive = true }
            }
        }
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "WinkCart",
                modifier = Modifier.padding(24.dp),
                fontSize = 48.sp,
                color = Rose40,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(400.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
