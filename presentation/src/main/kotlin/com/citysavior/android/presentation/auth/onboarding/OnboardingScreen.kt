package com.citysavior.android.presentation.auth.onboarding

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.citysavior.android.presentation.R
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.common.layout.DefaultLayout

@Composable
fun OnboardingScreen(
    startButtonClick: () -> Unit = {},
) {
    DefaultLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(52.dp),
                painter = painterResource(R.drawable.bell_icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.onboarding0),
                style = TextStyles.TITLE_LARGE2,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier =Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                IconTextRow(text = stringResource(R.string.onboarding1))
                IconTextRow(text = stringResource(R.string.onboarding2))
                IconTextRow(text = stringResource(R.string.onboarding3))
            }
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                painter = painterResource(R.drawable.onboarding_icon),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
            TextButton(
                shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                modifier = Modifier.background(
                    color = Colors.PRIMARY_BLUE,
                    shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
                ),
                contentPadding = PaddingValues(
                    horizontal = 50.dp,
                    vertical = 12.dp,
                ),
                onClick = startButtonClick,
            ) {
                Text(
                    text = stringResource(R.string.onboarding_start_button),
                    color = Color.White,
                    style = TextStyles.TITLE_MEDIUM2,
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
private fun IconTextRow(
    text :String = "",
){
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ){
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.check_icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
        )
    }
}

@Preview
@Composable
fun OnboardingScreenPreview2() {
    OnboardingScreen(
        startButtonClick = {

        }
    )
}