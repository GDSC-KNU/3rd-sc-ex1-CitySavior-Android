package com.citysavior.android.presentation.main.map.component

import androidx.compose.runtime.Composable
import com.citysavior.android.domain.model.report.Category
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import timber.log.Timber

@Composable
fun ReportMarker(
    latitude: Double,
    longitude: Double,
    weight: Int,
    category: Category = Category.OTHER,
    markerBitmap : BitmapDescriptor? = null,
    onClick: () -> Unit = {}
){
    val knu = LatLng(latitude, longitude)


    Marker(
        state = MarkerState(position = knu),
        icon = markerBitmap,
        onClick = {
            Timber.d("marker clicked")
            onClick()
            true
        }
    )
}

