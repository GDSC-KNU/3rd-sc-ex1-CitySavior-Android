package com.citysavior.android.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.user.UserInfo
import com.citysavior.android.domain.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val userInfo : StateFlow<Async<UserInfo>> get() = _userInfo
    private val _userInfo = MutableStateFlow<Async<UserInfo>>(Async.Loading)

    init {
        getUserInfo()
    }

    fun getUserInfo(){
        viewModelScope.launch {
            userRepository.getUserInfo().let {
                _userInfo.value = it
            }
        }
    }
}