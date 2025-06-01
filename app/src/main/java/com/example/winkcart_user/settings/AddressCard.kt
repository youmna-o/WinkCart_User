package com.example.winkcart_user.settings

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_user.R
import com.example.winkcart_user.data.model.settings.Address
import com.example.winkcart_user.ui.theme.CardBackgroundColor
import com.example.winkcart_user.ui.theme.HeaderTextColor
import com.example.winkcart_user.ui.theme.myPurple
import com.example.winkcart_user.utils.Constants.CARD_CARD_CORNER_RADIUS


@Composable
fun AddressCard(address: Address){

    Card(
        shape = RoundedCornerShape(CARD_CARD_CORNER_RADIUS),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
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
                    fontSize = 18.sp
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(start = 24.dp)) {
                Text(
                    text = address.contactPerson,
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
                    fontSize = 16.sp
                )


            }
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ){

                Checkbox(
                    checked = true,
                    onCheckedChange = { },
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


@Preview
@Composable
private fun Preview_AddressCard() {

    AddressCard(Address(title = "Home", country = "Egypt", address = "Chino Hills, CA 91709, United States", phone = "+20 114 070 8568", contactPerson = "Rofaida Sobhy"))
    
}