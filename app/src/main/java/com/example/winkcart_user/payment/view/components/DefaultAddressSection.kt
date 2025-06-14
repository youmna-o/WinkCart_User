package com.example.winkcart_user.payment.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.data.model.settings.address.CustomerAddress
import com.example.winkcart_user.ui.theme.HeaderTextColor

@Composable
fun DefaultAddressSection(address: CustomerAddress) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        Column(
            Modifier.padding(16.dp),
        ) {


            Row(

                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_address_card),
                    contentDescription = "about us Icon",
                    Modifier
                        .size(40.dp)

                )

                Text(
                    text = address.title,
                    color = HeaderTextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Change",
                    color = Color.Red,
                    modifier = Modifier.clickable { }
                )

            }


            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(start = 24.dp)) {
                Text(
                    text = address.name,
                    color = HeaderTextColor,
                    fontSize = 16.sp

                )

                Text(
                    text = address.phone,
                    color = HeaderTextColor,
                    fontSize = 16.sp
                )

                Text(
                    text = address.country,
                    color = HeaderTextColor,
                    fontSize = 16.sp
                )

                Text(
                    text = address.address,
                    color = HeaderTextColor,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


            }

        }
    }
}
