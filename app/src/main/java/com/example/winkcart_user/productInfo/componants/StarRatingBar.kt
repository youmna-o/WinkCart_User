package com.example.winkcart_user.productInfo.componants

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(rating: Float, maxStars: Int = 5 , size : Float) {
    val density = LocalDensity.current.density
    val starSize = (size * density).dp
    val starSpacing = (0.5f * density).dp

    val fullStars = rating.toInt()
    val hasHalfStar = (rating - fullStars) >= 0.5f

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val icon = when {
                i <= fullStars -> Icons.Filled.Star
                i == fullStars + 1 && hasHalfStar -> Icons.Rounded.StarHalf
                else -> Icons.Outlined.Star
            }

            val tintColor = if (i <= fullStars || (i == fullStars + 1 && hasHalfStar)) {
                Color(0xFFFFC700)
            } else {
                Color.Gray
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tintColor,
                modifier = Modifier
                    .size(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}