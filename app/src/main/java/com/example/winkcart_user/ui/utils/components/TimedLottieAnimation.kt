package com.example.winkcart_user.ui.utils.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import kotlinx.coroutines.delay


@Composable
fun TimedLottieAnimation(resId: Int, durationMillis: Long, message: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(durationMillis)
        isVisible = false
    }

    if (isVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress.value },
                modifier = Modifier.size(200.dp),

                )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}
