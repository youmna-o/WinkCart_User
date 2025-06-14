package com.example.winkcart_user.helperclasses

import android.content.Context
import android.location.Geocoder
import android.util.Log
import java.util.Locale

object MapUtils {
    fun getAddressFromLatLng(context: Context, lat: Double, lng: Double): Pair<String, String> {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val addressLine = addresses[0].getAddressLine(0) ?: "Unknown address"
                val countryName = addresses[0].countryName ?: "Unknown country"

                addressLine to countryName
            } else {
                "Address not found" to "Unknown country"
            }
        } catch (e: Exception) {
            "Geocoder failed: ${e.localizedMessage}" to "Unknown country"
        }
    }
}