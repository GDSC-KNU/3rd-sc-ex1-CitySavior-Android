package com.citysavior.android.presentation.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.main.map.component.ReportMarker
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
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
const val MARKER_ZOOM_LEVEL = 16f

@SuppressLint("PermissionLaunchedDuringComposition")
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
) {
    var cameraPositionSavePoint by remember {
        mutableStateOf(Point.fixture())
    }
    LaunchedEffect(Unit) {
        val flow = mapViewModel.getUserPoint()
        flow.collect { point ->
            when (point) {
                is Async.Loading -> {
                    Timber.d("loading")
                }
                is Async.Error -> {
                    val p = Point.fixture()
                    cameraPositionSavePoint = p
                    Timber.d("lat : ${p.latitude}, long : ${p.longitude}")
                    mapViewModel.getReports(p.latitude, p.longitude)
                    return@collect
                }
                is Async.Success -> {
                    val p = point.data
                    cameraPositionSavePoint = p
                    Timber.d("lat : ${p.latitude}, long : ${p.longitude}")
                    mapViewModel.getReports(p.latitude, p.longitude)
                    return@collect
                }
            }
        }
    }
    val context = LocalContext.current
    var isPermissionGranted by remember { mutableStateOf(hasLocationPermission(context)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(cameraPositionSavePoint.latitude,cameraPositionSavePoint.longitude), INITIAL_ZOOM_LEVEL)
    }
    val uiSettings = MapUiSettings(mapToolbarEnabled = false, zoomControlsEnabled = false, myLocationButtonEnabled = true)
    // Create a permission launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted, update the location
                    getCurrentLocation(context) { lat, long ->
                        Timber.d("lat : $lat, long : $long")
                    }
                    isPermissionGranted = true
                }
            })
    LaunchedEffect(isPermissionGranted) {
        isPermissionGranted = hasLocationPermission(context)
        Timber.d("위치 권한 isGranted : $isPermissionGranted")
        if (isPermissionGranted) {
            // Permission already granted, update the location
            getCurrentLocation(context) { lat, long ->
                Timber.d("위치정보 !!!!! lat : $lat, long : $long")
                mapViewModel.saveUserPoint(lat, long)
            }
        } else {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Timber.d("zoom : ${cameraPositionState.position.zoom}, position : ${cameraPositionState.position}")

    val reportPoints = mapViewModel.reports.collectAsStateWithLifecycle()


    val scope = rememberCoroutineScope()
    val deviceWidthPx = LocalView.current.resources.displayMetrics.widthPixels
    DisposableEffect(cameraPositionState.position.target) {
        val job = scope.launch {
            delay(400)
            val latLng = cameraPositionState.position.target
            val current = Point(latLng.latitude, latLng.longitude)

            val distance = cameraPositionSavePoint.calculateDistance(current) //현재위치와 이전위치의 거리 계산
            val zoom = cameraPositionState.position.zoom
            val width = zoom.toZoomToMetersPerPixel() * deviceWidthPx/4
            Timber.d("거리 계산 ! : $distance meter, | zoom: $zoom  px?meter: ${zoom.toZoomToMetersPerPixel()} ")
            Timber.d("deviceWidthPx : $deviceWidthPx, meter*px : ${width}")

            if(distance > width/1.5){//화면을 벗어나면 다시 요청
                mapViewModel.getReports(current.latitude, current.longitude)
                cameraPositionSavePoint = current
            }

        }
        onDispose {
            job.cancel()
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var createNewReport by rememberSaveable { mutableStateOf(false) }
    var selectedReportId: Long? by remember { mutableStateOf(null) }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if(isPermissionGranted){
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = MapProperties(
                    isMyLocationEnabled = true, minZoomPreference = 8f, maxZoomPreference = 20f,
                ),
            ) {
                when (reportPoints.value) {
                    is Async.Loading -> { Timber.d("loading") }
                    is Async.Error -> { Timber.d("error") }
                    is Async.Success -> {
                        val reports = (reportPoints.value as Async.Success).data
                        reports.forEach {
                            ReportMarker(
                                latitude = it.point.latitude,
                                longitude = it.point.longitude,
                                category = it.category,
                                weight = it.weight,
                                onClick = {
                                    selectedReportId = it.id
                                    mapViewModel.getDetailReport(it.id)
                                    showDialog = true
                                    val zoom = if(cameraPositionState.position.zoom < MARKER_ZOOM_LEVEL)
                                        MARKER_ZOOM_LEVEL
                                    else
                                        cameraPositionState.position.zoom

                                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                        LatLng(it.point.latitude, it.point.longitude),
                                        zoom
                                    )
                                    scope.launch {
                                        cameraPositionState.animate(cameraUpdate, 300)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }else{
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = MapProperties(
                    isMyLocationEnabled = false, minZoomPreference = 8f, maxZoomPreference = 20f,
                ),
            ) {
                when (reportPoints.value) {
                    is Async.Loading -> { Timber.d("loading") }
                    is Async.Error -> { Timber.d("error") }
                    is Async.Success -> {
                        val reports = (reportPoints.value as Async.Success).data
                        reports.forEach {
                            ReportMarker(
                                latitude = it.point.latitude,
                                longitude = it.point.longitude,
                                category = it.category,
                                weight = it.weight,
                                onClick = {
                                    selectedReportId = it.id
                                    mapViewModel.getDetailReport(it.id)
                                    showDialog = true
                                    val zoom = if(cameraPositionState.position.zoom < MARKER_ZOOM_LEVEL)
                                        MARKER_ZOOM_LEVEL
                                    else
                                        cameraPositionState.position.zoom

                                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                        LatLng(it.point.latitude, it.point.longitude),
                                        zoom
                                    )
                                    scope.launch {
                                        cameraPositionState.animate(cameraUpdate, 300)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            shape = CircleShape,
            containerColor = Colors.PRIMARY_BLUE,
            onClick = { createNewReport = true }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }

    /**
     * When Marker Clicked, Show Detail Info From BottomSheet
     */
    if (showDialog) {
        MarkerBottomSheet(
            onDismissRequest = { showDialog = false },
            selectedReportId = selectedReportId!!,
            reportPoints = reportPoints,
            mapViewModel = mapViewModel,
        )
    }

    if(createNewReport){
        Dialog(
            onDismissRequest = { createNewReport = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                securePolicy = SecureFlagPolicy.SecureOff
            ),
        ) {
            (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)
            CreateReportScreen(
                onBackIconClick = { createNewReport = false },
                onUploadButtonClick = { params->
                    mapViewModel.createReport(params)
                    createNewReport = false
                },

            )
        }
    }

}



/**
 * Calculate the weight of the marker based on the [zoom] level.
 * Max zoom level is 21f.
 */
private fun calculateWeight(zoom: Float): Int {
    val weight = when {
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


private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long)
            }
        }
        .addOnFailureListener { exception ->
            // Handle location retrieval failure
            exception.printStackTrace()
        }
}

fun Float.toZoomToMetersPerPixel(): Double {
    return 156543.03392 * Math.cos(this * Math.PI / 180) / Math.pow(2.0, this.toDouble())
}