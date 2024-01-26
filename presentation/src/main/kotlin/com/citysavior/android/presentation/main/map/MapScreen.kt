package com.citysavior.android.presentation.main.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen() {
    val knu = LatLng(35.890401, 128.612033)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(knu, 15f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = knu),
            title = "Korea Number One University",
            snippet = "경북대는 세계최고 대학이다."
        )
    }
}