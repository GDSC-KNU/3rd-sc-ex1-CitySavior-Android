package com.citysavior.android.presentation.main.home.component


import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.citysavior.android.presentation.R
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles


@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    title: String,
    progress : Pair<Int,Int>,
    @DrawableRes iconRes : Int,
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(152f/138f)
            .background(
                Colors.WIDGET_BG_GREY,
                shape = RoundedCornerShape(size = Sizes.WIDGET_ROUND)
            )
            .padding(Sizes.INTERVAL0)
    ) {
        Box(
            modifier = Modifier
                .size(33.dp)
                .background(
                    color = Colors.PRIMARY_BLUE,
                    shape = RoundedCornerShape(size = Sizes.WIDGET_ROUND)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(
                    id = iconRes
                ),
                contentDescription = "Search",
                tint = Color.White
            )
        }
        Spacer(modifier =Modifier.height(Sizes.INTERVAL0))
        Text(
            text = title,
            style = TextStyles.TITLE_MEDIUM2,
        )
        Spacer(modifier =Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            LinearProgressIndicator(
//                progress = 0.76f,
//                color = Color.Red,
//            )
            CustomProgress(
                width = 74.dp,
                height = 5.dp,
                progress = progress.first.toFloat()/progress.second.toFloat(),
                color = Colors.PRIMARY_BLUE,
            )
            CustomChip(
                text = "${progress.first}/${progress.second}",
                textStyle = TextStyles.CONTENT_SMALL2_STYLE.copy(
                    color = Color.White,
                ),
                size = DpSize(
                    width = Sizes.CHIP_WIDTH,
                    height = Sizes.CHIP_HEIGHT,
                ),
            )
        }

    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun CategoryItemPreview(){
    CategoryItem(
        title = "Damages",
        progress = Pair(9, 24),
        iconRes = R.drawable.build_icon_50
    )
}