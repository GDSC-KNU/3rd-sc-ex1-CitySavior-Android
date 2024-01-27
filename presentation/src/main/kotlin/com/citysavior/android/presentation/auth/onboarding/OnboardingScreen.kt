package com.citysavior.android.presentation.auth.onboarding

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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.citysavior.android.presentation.R
import com.citysavior.android.presentation.auth.AuthViewModel
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.common.layout.DefaultLayout
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    startButtonClick: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val composeCoroutineScope = rememberCoroutineScope()
    PreviewWrapper(
        startButtonClick = {
            composeCoroutineScope.launch {
                viewModel.onBoardingDone()
                startButtonClick()
            }
       },
    )
}

@Composable
private fun PreviewWrapper(
    startButtonClick: () -> Unit = {},
){
    DefaultLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1.5f))
            Image(
                modifier = Modifier.size(52.dp),
                painter = painterResource(R.drawable.bell_icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(20.dp))
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
            Spacer(modifier = Modifier.height(25.dp))
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 10.dp),
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
            Spacer(modifier = Modifier.weight(2f))
        }
    }
}

@Composable
private fun IconTextRow(
    text :String = "",
){
    Row(
        modifier = Modifier
            .padding(vertical = 6.dp),
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
            style = LocalTextStyle.current.copy(
                fontSize = 14.sp,
            )
        )
    }
}

@Preview
@Composable
fun OnboardingScreenPreview2() {
    PreviewWrapper()
}