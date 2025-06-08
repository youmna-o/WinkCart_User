package com.example.winkcart_user.data.remote.retrofit

import kotlin.random.Random

object MockDataSource {
     fun getRate(): Float {

        var rate =  Random.nextDouble(from = 2.5, until =5.0 )
        return  rate.toFloat()
    }

    fun getReview(): String {
        var reviewList = listOf("Great product, super soft material!","Size fits perfectly, but the price is a bit high.","The color is vibrant, looks better than the pictures",
            "Decent quality, though the material feels slightly thin.","Love the color, but the size runs a bit small.","Good product, but the price could be lower.",
            "Awesome value for the price, sturdy material.","The fit is spot-on, and the color pops!")
        return reviewList.random()

    }
}