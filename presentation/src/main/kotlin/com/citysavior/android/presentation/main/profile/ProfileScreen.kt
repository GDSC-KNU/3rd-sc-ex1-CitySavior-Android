package com.citysavior.android.presentation.main.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.user.UserInfo
import com.citysavior.android.presentation.common.component.ErrorScreen
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.common.layout.DefaultLayout
import com.citysavior.android.presentation.main.profile.component.AchievementItem
import com.citysavior.android.presentation.main.profile.component.ProfileBox
import com.citysavior.android.presentation.main.profile.component.ProfileStatItem


@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState = profileViewModel.userInfo.collectAsStateWithLifecycle()
    when(uiState.value){
        is Async.Error -> {
            ErrorScreen(message = (uiState.value as Async.Error).exception.message ?: "Unknown Error")
        }
        is Async.Loading->{
            DefaultLayout {
                Box(
                    modifier =Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ){
                    CircularProgressIndicator()
                }
            }
        }
        is Async.Success -> {
            val data = (uiState.value as Async.Success<UserInfo>).data
            DefaultLayout(
                title = "",
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    }
                }
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    ProfileBox(
                        title = "DamageTracker",
                        content = "View damage at a glance",
                    )
                    Spacer(modifier = Modifier.height(Sizes.INTERVAL_LARGE4))
                    Text(
                        text = "View damage at a glance",
                        style = TextStyles.TITLE_MEDIUM2,
                    )
                    Spacer(modifier = Modifier.height(Sizes.INTERVAL_MEDIUM2))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ProfileStatItem(
                            modifier = Modifier.weight(1f),
                            label = data.totalReportCount.toString(),
                            content = "Total reported\ndamage",
                        )
                        ProfileStatItem(
                            modifier = Modifier.weight(1f),
                            label = data.totalRepairedCount.toString(),
                            content = "Repairs\ncompleted",
                        )
                        ProfileStatItem(
                            modifier = Modifier.weight(1f),
                            label = data.achieveCollectCount.toString(),
                            content = "Rewards\ncollected",
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "Achievements",
                        style = TextStyles.TITLE_MEDIUM2,
                    )
                    Spacer(modifier = Modifier.height(Sizes.INTERVAL2))
                    data.achieveProgressingList.forEach {achieveProgress ->
                        AchievementItem(
                            modifier = Modifier.padding(
                                vertical = Sizes.INTERVAL4,
                                horizontal = 1.dp,
                            ),
                            title = achieveProgress.achievement.name,
                            content = achieveProgress.achievement.description,
                            progress = Pair(achieveProgress.progressCount, achieveProgress.achievement.goalCount),
                        )
                    }
                }

            }
        }
    }

}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}