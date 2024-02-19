package com.citysavior.android.presentation.main.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.presentation.common.component.CustomTextEditField
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerBottomSheet(
    selectedReportId: Long,
    reportPoints: State<Async<List<ReportPoint>>>,
    mapViewModel: MapViewModel,
    onDismissRequest: () -> Unit,
){
    val sheetState = rememberModalBottomSheetState()
    val scrollState = rememberLazyListState()
    val report =
        (reportPoints.value as Async.Success).data.find { it.id == selectedReportId }!!
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        shape = BottomSheetDefaults.HiddenShape,
        scrimColor = Color.Transparent,
        dragHandle = {
             Box(
                 modifier =Modifier.fillMaxWidth()
             ){
                 HorizontalDivider()
             }
        },
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

@Composable
fun ModalMiddle(
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
