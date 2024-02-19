package com.citysavior.android.presentation.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.ReportStatistics
import com.citysavior.android.presentation.common.component.ErrorScreen
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.common.layout.DefaultLayout
import com.citysavior.android.presentation.main.home.component.CategoryItem
import com.citysavior.android.presentation.main.home.component.CustomChip
import com.citysavior.android.presentation.main.home.component.DailyProgress
import com.citysavior.android.presentation.main.home.component.DailyProgressLoading


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = homeViewModel.reportStatistics.collectAsStateWithLifecycle()
    when(uiState.value){
        is Async.Loading,
        is Async.Success
        -> {
            val data = (uiState.value as? Async.Success<ReportStatistics>)?.data
            DefaultLayout(
                title = "Home",
                actions = {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Account", tint = Color.Black)
                }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Row {
                        Text(
                            "Report Dangerous Areas",
                            style = TextStyles.TITLE_MEDIUM2.copy(
                                fontWeight = TextStyles.MEDIUM,
                            ),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        CustomChip(
                            text = "Report",
                            textStyle = TextStyles.CONTENT_TEXT3_STYLE,
                            size = DpSize(
                                width = 120.dp,
                                height = 32.dp,
                            ),
                            backgroundColor = Colors.WIDGET_BG_GREY,
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    CustomChip(
                        text = "Overview",
                        textStyle = TextStyles.CONTENT_TEXT3_STYLE.copy(
                            color = Color.White,
                        ),
                        size = DpSize(
                            width = 120.dp,
                            height = 32.dp,
                        ),
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    data?.let {
                        DailyProgress(
                            title = "Daily progress",
                            subTitle = "Here you can see your reported damages",
                            progress = Pair(data.resolvedReports, data.totalReports),
                        )
                    } ?: DailyProgressLoading()
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "Categories",
                        style = TextStyles.TITLE_LARGE2,
                    )
                    Spacer(modifier = Modifier.height(Sizes.INTERVAL2))
                    data?.let {
                        for(i in 0 until (data.statisticsDetails.size/2)){
                            val left = data.statisticsDetails[i]
                            val right = data.statisticsDetails[i+1]
                            Row{
                                CategoryItem(
                                    modifier = Modifier.weight(1f),
                                    subTitle = "Sub Title",
                                    title = left.category.korean,
                                    progress = Pair(left.resolvedReports, left.totalReports),
                                )
                                Spacer(modifier = Modifier.width(Sizes.INTERVAL0))
                                CategoryItem(
                                    modifier = Modifier.weight(1f),
                                    subTitle = "Sub Title",
                                    title = right.category.korean,
                                    progress = Pair(right.resolvedReports, right.totalReports),
                                )
                            }
                            Spacer(modifier = Modifier.height(Sizes.INTERVAL0))
                        }
                        if(data.statisticsDetails.size%2 == 1){
                            val last = data.statisticsDetails.last()
                            Row {
                                CategoryItem(
                                    modifier = Modifier.weight(1f),
                                    subTitle = "Sub Title",
                                    title = last.category.korean,
                                    progress = Pair(last.resolvedReports, last.totalReports),
                                )
                                Spacer(modifier = Modifier.width(Sizes.INTERVAL0))
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }

            }
        }
        is Async.Error -> {
            ErrorScreen(message = (uiState.value as Async.Error).exception.message ?: "Unknown Error")
        }
    }


}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}