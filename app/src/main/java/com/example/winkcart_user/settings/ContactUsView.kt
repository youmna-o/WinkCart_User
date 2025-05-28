package com.example.winkcart_user.settings

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.utils.Constants
import com.example.winkcart_user.utils.Constants.SCREEN_PADDING
import com.example.winkcart_user.ui.theme.BackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor
import androidx.core.net.toUri

@Preview
@Composable
private fun ContactUsScreen() {
    val localContext = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(SCREEN_PADDING)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(text= stringResource(R.string.contact_us),color = HeaderTextColor , fontSize = 32.sp, fontWeight = FontWeight.Bold )
            Spacer(modifier = Modifier.height(30.dp))
            Text(stringResource(R.string.contact_us_st))
            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(R.string.contact_us_email), modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Constants.CONTACT_US_EMAIL.toUri()
                }
                localContext.startActivity(intent)
            })

            Spacer(modifier = Modifier.height(8.dp))

            Text(stringResource(R.string.contact_us_phone), modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Constants.CONTACT_US_PHONE.toUri()
                }
                localContext.startActivity(intent)
            })



        }
    }
}
