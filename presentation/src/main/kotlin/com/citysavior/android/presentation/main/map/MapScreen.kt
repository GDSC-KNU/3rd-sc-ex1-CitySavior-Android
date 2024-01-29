package com.citysavior.android.presentation.main.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.presentation.common.component.CustomTextEditField
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.main.map.component.ReportMarker
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import timber.log.Timber

const val INITIAL_ZOOM_LEVEL = 15f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        mapViewModel.getReports(35.890401, 128.612033)
    }
    val context = LocalContext.current
    val knu = LatLng(35.890401, 128.612033)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(knu, INITIAL_ZOOM_LEVEL)
    }
    val uiSettings = remember { MapUiSettings(mapToolbarEnabled = false) }
    val properties = remember {
        MapProperties(
            isMyLocationEnabled = false, minZoomPreference = 8f, maxZoomPreference = 20f
        )
    }

    Timber.d("zoom : ${cameraPositionState.position.zoom}, position : ${cameraPositionState.position}")

    val reportPoints = mapViewModel.reports.collectAsStateWithLifecycle()


    /**
     * Calculate marker Size by throttle 400ms
     */
//    val composeCorutineScope = rememberCoroutineScope()
//    DisposableEffect(cameraPositionState.position.zoom) {
//        val job = composeCorutineScope.launch {
//            delay(400)
//            dstSize = calculateWeight(cameraPositionState.position.zoom)
//        }
//        onDispose {
//            job.cancel()
//        }
//    }

    var showDialog by remember { mutableStateOf(false) }
    var selectedReportId: Long? by remember { mutableStateOf(null) }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        properties = properties,
    ) {

        when (reportPoints.value) {
            is Async.Loading -> {
                Timber.d("loading")
            }

            is Async.Success -> {
                val reports = (reportPoints.value as Async.Success).data
                reports.forEach {
                    ReportMarker(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        markerBitmap = null,
                        onClick = {
                            selectedReportId = it.id
                            mapViewModel.getDetailReport(it.id)
                            showDialog = true
                        }
                    )
                }
            }

            is Async.Error -> {
                Timber.d("error")
            }
        }
        MarkerComposable(
            state = MarkerState(position = knu),
        ) {
            Text(text = "this is compose marker")
        }

    }

    /**
     * When Marker Clicked, Show Detail Info From BottomSheet
     */
    if (showDialog) {
        val sheetState = rememberModalBottomSheetState()
        val scrollState = rememberScrollState()
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
            val isExpandedCondition =
                scrollState.value > 0 || (sheetState.currentValue == SheetValue.Expanded) && (sheetState.targetValue == SheetValue.Expanded)

            val detail = report as? ReportPointDetail
            Text(
                text = "${sheetState.currentValue} : ${sheetState.targetValue}",
            )
            Text(
                text = "${scrollState.value}",
            )
            if (detail == null) { // Loading
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(color = Color.White),

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

                ModalMiddle(detail = detail, scrollState = scrollState)

            }

        }

    }

}

@Composable
fun ModalTop(
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
                        style = TextStyles.TITLE_MEDIUM2
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

@Composable
fun ModalMiddle(
    detail: ReportPointDetail,
    isExpanded: Boolean = false,
    scrollState : ScrollState,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        AsyncImage(
            modifier = Modifier
                .aspectRatio(1f),
//                            .clip(RoundedCornerShape(Sizes.WIDGET_ROUND)),
            model = detail.imgUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(12.dp))
        detail.comments.forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ){
                Text(
                    text = it.content,
                    style = TextStyles.CONTENT_TEXT2_STYLE.copy(
                        color = Colors.GREY_TEXT
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = it.createdDate.toString(),
                    style = TextStyles.CONTENT_TEXT2_STYLE.copy(
                        color = Colors.GREY_TEXT
                    ),
                )
            }
        }


        var comment by remember { mutableStateOf("") }

        CustomTextEditField(
            value = comment,
            label = "댓글 입력",
            onValueChange = {comment = it},
            keyboardOptions = KeyboardOptions(
                imeAction = androidx.compose.ui.text.input.ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),

        )

        val sp = WindowInsets.systemBars.asPaddingValues()
        val bottom = sp.calculateBottomPadding()
        //기기 바텀 네비게이션바 높이만큼 띄워주기 위한 Spacer
        Spacer(modifier = Modifier.height(bottom))
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ModelPreView() {
    ModalTop(ReportPointDetail.fixture(), isExpanded = false)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ModalMiddlePreView() {
    ModalMiddle(ReportPointDetail.fixture(), false, rememberScrollState())
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