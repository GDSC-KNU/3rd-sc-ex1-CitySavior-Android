package com.citysavior.android.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles


@Composable
fun CustomTextEditField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    backgroundColor : Color = Colors.WHITE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    value: String,
    onValueChange: (String) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    label: String="",
    startPadding : Dp = Sizes.INTERVAL1,
    isError : Boolean = false,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = Sizes.BUTTON_HEIGHT)
            .fillMaxWidth()
            .background(color = backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = startPadding),
            textStyle = TextStyles.CONTENT_TEXT2_STYLE,
            maxLines = 1,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            value = value,
            interactionSource = interactionSource,
            onValueChange = onValueChange,
            keyboardActions = keyboardActions
        ){inner ->
            if(value.isEmpty()){
                Text(
                    text = label,
                    style = TextStyles.CONTENT_TEXT3_STYLE.copy(
                        color = Colors.GREY_TEXT
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = Sizes.INTERVAL4),
                )
            }
            inner()
        }
    }
}