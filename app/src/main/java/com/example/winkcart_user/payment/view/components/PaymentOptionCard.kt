package com.example.winkcart_user.payment.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PaymentOptionCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: Int
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            RadioButton(selected = isSelected, onClick = onClick)

            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color.Unspecified,
                modifier = Modifier.padding(end = 16.dp)
                    .size(24.dp)
            )
        }
    }
}
