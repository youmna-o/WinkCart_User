package com.example.winkcart_user.settings.view.address.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.winkcart_user.R
import com.example.winkcart_user.settings.viewmodel.map.PlacesViewModel
import com.example.winkcart_user.theme.myPurple
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun PlacePicker(placesViewModel: PlacesViewModel, onConfirm:(LatLng) -> Unit) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(30.0444, 31.2357), 10f)
    }
    val selectedLocation by  placesViewModel.addressLatLon.collectAsState()

    val addressName by placesViewModel.addressName.collectAsState()


    Column (modifier = Modifier.fillMaxSize()){
        SearchPlaceBar(
            placesViewModel,
            onQueryChange = {placesViewModel.changeQuery(it)},
            onPlaceSelected = {address ->
                placesViewModel.fetchPlaceById(address) {latlon->
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latlon, 15f)
                }

            }
        )
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = {latLng ->  placesViewModel.updateLocation(latLng) }){
            selectedLocation?.let {
                Marker(
                    state = MarkerState(position =it),
                    title = addressName ?: stringResource(R.string.selected_location),
                    draggable = true,
                    onClick = {false}
                )
            }

        }
        Button(
            onClick = { selectedLocation?.let { latLng -> onConfirm(latLng)} },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = myPurple/*colorResource(id = R.color.Default_Colour2)*/,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(R.string.confirm_location))
        }
    }

}