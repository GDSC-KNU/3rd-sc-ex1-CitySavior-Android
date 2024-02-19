package com.citysavior.android.presentation.main.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.common.utils.shimmerLoadingAnimation
import kotlin.math.roundToInt

@Composable
fun DailyProgress(
    modifier: Modifier = Modifier,
    title : String,
    subTitle: String,
    progress : Pair<Int,Int>,
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.DAILY_PROGRESS_HEIGHT)
            .background(
                Colors.WIDGET_BG_GREY,
                shape = RoundedCornerShape(size = Sizes.WIDGET_ROUND)
            )
            .padding(Sizes.INTERVAL_LARGE4),
    ) {
        Text(
            text = title,
            style = TextStyles.TITLE_LARGE2,
        )
        Spacer(modifier = Modifier.height(Sizes.INTERVAL4))
        Text(
            text = subTitle,
            style = TextStyles.CONTENT_SMALL1_STYLE,
        )
        Spacer(modifier = Modifier.weight(1f))
        if(progress.second != 0){
            Text(
                text = "${((progress.first).toFloat()/(progress.second).toFloat()*10000).roundToInt()/100.0}%",
                style = TextStyles.TITLE_MEDIUM2,
            )
        }else{
            Text(
                text = "0%",
                style = TextStyles.TITLE_MEDIUM2,
            )
        }
        Spacer(modifier = Modifier.height(Sizes.INTERVAL3))
        CustomProgress(
            width = 340.dp,
            height = 5.dp,
            progress = progress.first.toFloat()/progress.second.toFloat(),
            color = Colors.PRIMARY_BLUE,
        )

    }
}

@Composable
fun DailyProgressLoading(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(Sizes.DAILY_PROGRESS_HEIGHT)
            .background(
                Colors.WIDGET_BG_GREY,
                shape = RoundedCornerShape(size = Sizes.WIDGET_ROUND)
            ).shimmerLoadingAnimation(),
    ){}
}

@Composable
@Preview
fun DailyProgressPreview(){
    DailyProgress(
        title = "Damages",
        subTitle = "5 New",
        progress = Pair(7,24),
    )
}