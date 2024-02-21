package com.citysavior.android.presentation.main.map.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.presentation.main.map.getPaintRes
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import timber.log.Timber

@Composable
fun ReportMarker(
    latitude: Double,
    longitude: Double,
    weight: Int,
    category: Category = Category.OTHER,
    onClick: () -> Unit = {}
){
    val latLng = LatLng(latitude, longitude)
    val width = (40*0.9).dp
    val height = (48*0.9).dp
    val iconSize = 22.dp

    /**
     * 파스텔톤의 색상표현
     */
    val strokeColor = when(weight){
        0-> Color(0xFFCCFFCC)
        1-> Color(0xFFFFFFCC)
        else -> Color(0xFFFFCCCC)
    }

    MarkerComposable(
        state = MarkerState(position = latLng),
        draggable = true,
        onClick = {
            Timber.d("marker clicked")
            onClick()
            true
        }
    ) {
        DrawConicalShape(
            width = width,
            height = height,
            strokeColor = strokeColor
        )
        Icon(
            modifier = Modifier
                .padding((width-iconSize)/2)
                .size(iconSize),
            painter = painterResource(
                id = category.getPaintRes()
            ),
            contentDescription = null
        )
    }
}


/**
 * 콘모양을 그리는 컴포저블
 * @param startAngle : 시작 각도. 360도 기준으로 시계방향으로 증가, 0도는 3시 방향에서 시작
 * @param sweepAngle : 각도의 범위
 */
@Composable
fun DrawConicalShape(
    width: Dp = 120.dp,
    height: Dp = 130.dp,
    stroke: Dp = 2.dp,
    startAngle: Float = 140f,
    sweepAngle: Float = 260f,
    strokeColor: Color = Color.Red,
) {
    Canvas(modifier = Modifier
        .size(width,height)
    ) {
        val sub = size.height - size.width

        val centerX = size.width / 2
        val centerY = size.height / 2 - sub/2
        val radius = size.width / 2 - stroke.toPx() // 줄의 두께 고려
        val paint = Paint().apply {
            color = strokeColor
            style = PaintingStyle.Stroke
            strokeWidth = stroke.toPx() // 콘 모양의 두께
            strokeCap = StrokeCap.Round // 라인의 끝 부분을 둥글게 만듦
        }
        val path = Path().apply {
            moveTo(centerX, centerY + sub) // 시작점 설정
            arcTo(
                rect = Rect(centerX - radius, centerY - radius, centerX + radius, centerY + radius), // 호를 포함하는 사각형
                startAngleDegrees = startAngle,
                sweepAngleDegrees = sweepAngle,
                forceMoveTo = true
            )
            lineTo(centerX, size.height - stroke.toPx())
            close()
        }
        this.drawPath(
            path = path,
            color = Color.White, // 콘형 모양 영역의 색상을 빨간색으로 지정합니다.
            style = Fill // 영역을 채우는 스타일로 설정합니다.
        )
        this.drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(width = stroke.toPx()),
        )
    }
}

@Preview(backgroundColor = 0xFF555555)
@Composable
fun DrawConicalShapePreview(
) {
    Box(
        modifier= Modifier
            .size(120.dp, 320.dp)
            .background(Color.White)
    ) {
        DrawConicalShape()
    }
}