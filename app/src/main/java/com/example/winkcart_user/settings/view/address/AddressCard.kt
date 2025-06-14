package com.example.winkcart_user.settings.view.address

import android.util.Log
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.data.model.settings.address.CustomerAddress
import com.example.winkcart_user.ui.theme.CardBackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor
import com.example.winkcart_user.ui.theme.myPurple
import com.example.winkcart_user.utils.Constants.CARD_CARD_CORNER_RADIUS
import com.google.maps.android.compose.GoogleMap


@Composable
fun AddressCard(
    customerId: Long,
    address: CustomerAddress, defaultCheckAction: ()-> Unit, editAction: (Long, Long) -> Unit
){

    Log.i("TAG", "AddressCard: $address")
    Card(
        shape = RoundedCornerShape(CARD_CARD_CORNER_RADIUS),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
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
                    text = stringResource(R.string.edit),
                    color = myPurple,
                    fontSize = 18.sp,
                    modifier = Modifier.clickable {
                        editAction.invoke(customerId,address.id)
                    }
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
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ){

                Checkbox(
                    checked = address.default,
                    onCheckedChange = { defaultCheckAction.invoke() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Black,
                        checkmarkColor = Color.White
                    )

                )

                Text(
                    text = stringResource(R.string.address_card_checkbox_text),
                    color = HeaderTextColor,
                    fontSize = 16.sp,
                )
            }
        }

    }

}

