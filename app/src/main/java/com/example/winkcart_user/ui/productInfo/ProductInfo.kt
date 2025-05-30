package com.example.winkcart_user.ui.productInfo

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.winkcart_user.ui.theme.myPurple

@Composable
fun ProductInfo(navController: NavController, scrollState: ScrollState){
    Column (modifier = Modifier.fillMaxSize().verticalScroll(scrollState)){
      LazyRow (){
          items(5){ item ->
              ImageCard()

          }
      }
        Box (modifier = Modifier.height(500.dp).fillMaxWidth().background(color = myPurple)){ Text("ll") }
        Box (modifier = Modifier.height(400.dp).fillMaxWidth().background(color = Color.White)){ Text("ll") }
        Box (modifier = Modifier.height(500.dp).fillMaxWidth().background(color = myPurple)){ Text("ll") }

    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCard(){
    Card(
        modifier = Modifier
            .width(274.dp)
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
            containerColor = Color.Red.copy(alpha = 0.3f)
        ),

        ){
        GlideImage(
            model = "https://cdn.shopify.com/s/files/1/0758/1132/4152/files/product_21_image1.jpg?v=1748153911",
            contentDescription = "image",
            Modifier
                //  .size(100.dp)
                 .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
                //.background(color = Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                //.clip(RoundedCornerShape(16.dp))
                .fillMaxSize(),


                    contentScale = ContentScale.FillWidth
        )

    }
}

