package com.example.winkcart_user.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.Utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor

@Preview
@Composable
private fun ContactUsView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(SCREEN_PADDING)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally


        ) {

            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "app Icon",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(16.dp))

            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "WinkyCart is a smart shopping app built to simplify your online shopping experience. " +
                        "We provide access to a wide range of products across fashion, tech, and lifestyle categories.",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This app was developed as our graduation project at the Information Technology Institute (ITI).",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Development Team:",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTextColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            val teamMembers = listOf(
                "Arwa Khaled Mohamed",
                "Rofaida Sobhy Adwi",
                "Mustafa Mahmoud Ahmed",
                "Yomna Ashraf Elsayed"
            )

            teamMembers.forEach {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    color = HeaderTextColor
                )
            }


        }
    }
}