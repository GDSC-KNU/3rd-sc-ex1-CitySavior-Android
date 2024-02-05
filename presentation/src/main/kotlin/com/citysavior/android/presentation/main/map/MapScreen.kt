package com.citysavior.android.presentation.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.presentation.common.component.CustomTextEditField
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
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
@OptIn(ExperimentalMaterial3Api::class)
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
    DisposableEffect(cameraPositionState.position.target) {
        val job = scope.launch {
            delay(400)
            val latLng = cameraPositionState.position.target
            val current = Point(latLng.latitude, latLng.longitude)
            val distance = cameraPositionSavePoint.calculateDistance(current)
            val zoom = cameraPositionState.position.zoom
            Timber.d("거리 계산 ! : $distance, | zoom: ${zoom}  distance*zoom : ${adjustDistanceBasedOnZoom(distance, zoom.toDouble())}")
            if(distance < 0.001){
                return@launch
            }
            if(adjustDistanceBasedOnZoom(distance, zoom.toDouble()) > 0.01){
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
                                markerBitmap = null,
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
                                markerBitmap = null,
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
        val sheetState = rememberModalBottomSheetState()
        val scrollState = rememberLazyListState()
        val report =
            (reportPoints.value as Async.Success).data.find { it.id == selectedReportId }!!
        val scope = rememberCoroutineScope()
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showDialog = false },
            shape = BottomSheetDefaults.HiddenShape,
            scrimColor = Color.Transparent,
            dragHandle = null,
            windowInsets = WindowInsets(top = 80),
        ) {
            val isExpandedCondition =(sheetState.currentValue == SheetValue.Expanded) && (sheetState.targetValue == SheetValue.Expanded)

            val detail = report as? ReportPointDetail
            if (detail == null) { // Loading
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(color = Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else { // Success
                ModalTop(
                    detail = detail,
                    isExpanded = isExpandedCondition,
                    onIconClick = {
                        if (sheetState.currentValue == SheetValue.Expanded) {
                            scope.launch {
                                sheetState.partialExpand()
                            }
                        }
                    }
                )

                ModalMiddle(
                    detail = detail,
                    isExpanded = isExpandedCondition,
                    scrollState = scrollState,
                    onAddCommentClicked = {
                        mapViewModel.createComment(detail.id, it)
                    }
                )

            }

        }

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
                onUploadButtonClick = { createNewReport = false },
            )
        }
    }

}

@Composable
private fun ModalTop(
    detail: ReportPointDetail,
    onIconClick : () -> Unit = {},
    isExpanded: Boolean = false,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f)
            .background(color = Color.White)
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    Row{

                        Icon(
                            modifier= Modifier.clickable{
                              onIconClick()
                            },
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Text(
                    text = detail.category.korean,
                    style = TextStyles.TITLE_LARGE1
                )
                Spacer(Modifier.weight(1f))
            }
        }
        AnimatedVisibility(
            visible = !isExpanded,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(3f)
                ) {
                    Text(
                        text = detail.category.korean,
                        style = TextStyles.TITLE_LARGE1
                    )
                    Text(
                        detail.description,
                        style = TextStyles.CONTENT_TEXT2_STYLE.copy(
                            fontWeight = TextStyles.MEDIUM
                        )
                    )

                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = detail.reportDate.toString(),
                    style = TextStyles.CONTENT_TEXT2_STYLE.copy(
                        color = Colors.GREY_TEXT
                    ),
                )
            }

        }

    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ModalMiddle(
    detail: ReportPointDetail,
    isExpanded: Boolean = false,
    scrollState : LazyListState,
    onAddCommentClicked : (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    val sp = WindowInsets.systemBars.asPaddingValues()
    val bottom = sp.calculateBottomPadding()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.background(Color.White)
        ) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f),
                    model = detail.imgUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            item {
                detail.comments.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = Sizes.SMALL_H_PADDING),
                        ) {
                            Text(
                                text = it.content,
                                style = TextStyles.CONTENT_TEXT1_STYLE
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = it.createdDate.toString(),
                                style = TextStyles.CONTENT_SMALL1_STYLE.copy(
                                    color = Colors.GREY_TEXT
                                ),
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }
                    if (detail.comments.last() != it) {
                        HorizontalDivider(
                            color = Colors.DIVIDER_GREY,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

            }
            item {
                Spacer(modifier = Modifier.height(bottom))
            }

        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)) {
            var comment by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                CustomTextEditField(
                    value = comment,
                    label = "댓글 입력",
                    onValueChange = { comment = it },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (comment.isEmpty()) return@KeyboardActions
                            onAddCommentClicked(comment)
                            comment = ""
                        }
                    ),
                )
                if(comment.isNotEmpty()){
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                            .clickable {
                                focusManager.clearFocus()
                                if (comment.isEmpty()) return@clickable
                                onAddCommentClicked(comment)
                                comment = ""
                            }
                            .background(
                                color = Colors.PRIMARY_BLUE,
                                shape = RoundedCornerShape(2.dp),
                            )
                            .padding(vertical = 4.dp, horizontal = 10.dp)
                    ){
                        Text(
                            text = "등록",
                            style = TextStyles.CONTENT_TEXT3_STYLE.copy(
                                color = Color.White,
                            ),
                        )
                    }
                }
            }
            //기기 바텀 네비게이션바 높이만큼 띄워주기 위한 Spacer
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(bottom)
                .background(Color.White))
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
fun adjustDistanceBasedOnZoom(originalDistance: Double, zoom: Double): Double {
    // 사용자가 확대하면 거리를 늘어나게, 축소하면 거리를 줄어들게 조절
    // 여기서는 간단한 비례식을 사용함. 원하는 방식으로 수정 가능
    val adjustedDistance = originalDistance * Math.pow(2.0, zoom - 15.0)
    return adjustedDistance
}