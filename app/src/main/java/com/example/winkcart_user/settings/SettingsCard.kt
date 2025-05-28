package com.example.winkcart_user.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.winkcart_user.ui.theme.CardBackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor

@Preview

@Composable
fun SettingsCard(settingIcon: Int = R.drawable.ic_about_us,settingName:Int = R.string.about_us){

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor
        )
    ) {

        Row(
            Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(settingIcon),
                contentDescription = "about us Icon",
                Modifier
                    .size(50.dp)

            )



            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(settingName),
                color = HeaderTextColor,
                fontSize = 24.sp,


            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.ic_arrow),
                contentDescription = "arrow Icon",
                Modifier
                    .size(50.dp)

            )
        }


    }

}