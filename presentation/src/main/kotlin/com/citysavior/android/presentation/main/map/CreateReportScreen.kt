package com.citysavior.android.presentation.main.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.citysavior.android.presentation.common.component.CustomTextEditField
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.common.utils.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(
    onBackIconClick : () -> Unit = {},
) {
    var description by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ){
        TopAppBar(
            modifier = Modifier.background(Color.White),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
            ),
            title = {
                Text(
                    text = "신고하기",
                    style = TextStyles.TITLE_MEDIUM2,
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier.noRippleClickable { onBackIconClick() },
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )
            },
        )
        Text(
            text = "Upload Image",
            style = TextStyles.TITLE_MEDIUM2,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .background(Colors.WIDGET_BG_GREY)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row{
            TextButton(
                shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                modifier = Modifier
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                    )
                    .background(
                        color = Colors.PRIMARY_BLUE,
                        shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
                    )
                    .weight(1f),
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                ),
                onClick = {},
            ) {
                Text(
                    text = "Take photo",
                    color = Color.White,
                    style = TextStyles.CONTENT_SMALL1_STYLE,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            TextButton(
                shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                modifier = Modifier
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
                    )
                    .weight(1f),
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                ),
                onClick = {},
            ) {
                Text(
                    text = "Browse gallery",
                    color = Colors.BLACK,
                    style = TextStyles.CONTENT_SMALL1_STYLE,
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Add description",
            style = TextStyles.CONTENT_TEXT3_STYLE,
        )
        Spacer(modifier = Modifier.height(20.dp))

        CustomTextEditField(
            modifier = Modifier.background(
                color = Colors.WIDGET_BG_GREY, shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
            ),
            value = description,
            onValueChange = { description = it },
            backgroundColor = Color.Transparent,
            startPadding = 12.dp,
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Select Category",
            style = TextStyles.CONTENT_TEXT3_STYLE,
        )

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
            modifier = Modifier
                .background(
                    color = Colors.PRIMARY_BLUE,
                    shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
                )
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 50.dp,
                vertical = 12.dp,
            ),
            onClick = {},
        ) {
            Text(
                text = "Upload",
                color = Color.White,
                style = TextStyles.TITLE_MEDIUM2,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
