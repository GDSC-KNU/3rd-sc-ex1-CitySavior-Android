package com.citysavior.android.presentation.main.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.citysavior.android.presentation.R
import com.citysavior.android.presentation.common.layout.DefaultLayout
import com.citysavior.android.presentation.main.map.component.ReportMarker
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

const val INITIAL_ZOOM_LEVEL = 15f

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val knu = LatLng(35.890401, 128.612033)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(knu, INITIAL_ZOOM_LEVEL)
    }
    val uiSetttings = remember {
        MapUiSettings(mapToolbarEnabled = false,)
    }
    val properties by remember {
        mutableStateOf(MapProperties(
            isMyLocationEnabled = true,
        ))
    }
    var showDialog by remember { mutableStateOf(false) }

    Timber.d("zoom : ${cameraPositionState.position.zoom}")
    if(showDialog){
        Dialog(onDismissRequest = { showDialog = false }) {
            Text(text = "hello")

        }
    }

    val items = listOf(
        LatLng(35.895401, 128.612033),
        LatLng(35.89231, 128.61804),
        LatLng(35.89431, 128.61404),
        LatLng(35.89631, 128.61204),
    )



    /**
     * Calculate marker Size by throttle 400ms
     */
    var dstSize by remember { mutableIntStateOf(calculateWeight(INITIAL_ZOOM_LEVEL)) }
    val composeCorutineScope = rememberCoroutineScope()
    DisposableEffect(cameraPositionState.position.zoom) {
        val job = composeCorutineScope.launch {
            delay(400)
            dstSize = calculateWeight(cameraPositionState.position.zoom)
        }
        onDispose {
            job.cancel()
        }
    }

    DefaultLayout {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSetttings,
        ) {
            items.forEach {
                val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.marker_blue)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, dstSize, dstSize, true)
                val markerBitmap = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                ReportMarker(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    markerBitmap = markerBitmap,
                    onClick = {
                        showDialog = true
                    }
                )
            }

        }
    }

}


/**
 * Calculate the weight of the marker based on the [zoom] level.
 * Max zoom level is 21f.
 */
private fun calculateWeight(zoom: Float): Int {
    val weight =  when {
        zoom > 20f -> 5f
        zoom > 18f -> 4f
        zoom > 16f -> 3f
        zoom > 13.5f -> 2f
        zoom > 11f -> 1f
        else -> 0.5f
    }
    Timber.d("계산결과 : $weight")
    return (weight * 96).toInt()
}