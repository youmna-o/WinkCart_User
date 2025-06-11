package com.example.winkcart_user.settings.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.ui.theme.CardBackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor
import com.example.winkcart_user.utils.Constants.CARD_CARD_CORNER_RADIUS

@Preview
@Composable
fun SettingsCard(settingIcon: Int = R.drawable.ic_about_us,
                 settingName:Int = R.string.about_us,
                 subtitleText: String = "",
                 onClick: () -> Unit = {}){

    Card(
        shape = RoundedCornerShape(CARD_CARD_CORNER_RADIUS),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = onClick
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
                fontSize = 18.sp
            )


            Spacer(modifier = Modifier.weight(1f))
            if (subtitleText.isNotBlank()) {
                Text(
                    text = subtitleText,
                    color = HeaderTextColor.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_arrow),
                contentDescription = "arrow Icon",
                Modifier
                    .size(50.dp)

            )
        }


    }

}