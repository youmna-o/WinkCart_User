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
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor

@Preview
@Composable
private fun AboutUsView() {
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
                text = stringResource(R.string.about_us_st1),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.about_us_st2),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.development_team),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTextColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            val teamMembers = listOf(
                R.string.team_member1,
                R.string.team_member2,
                R.string.team_member3,
                R.string.team_member4
            )

            teamMembers.forEach {
                Text(
                    text = stringResource(it),
                    fontSize = 16.sp,
                    color = HeaderTextColor
                )
            }


        }
    }
}