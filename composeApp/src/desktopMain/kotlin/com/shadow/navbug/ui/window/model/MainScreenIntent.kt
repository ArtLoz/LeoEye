package com.shadow.navbug.ui.window.model

import com.shadow.navbug.domain.main.model.LeoDevice

sealed interface MainScreenIntent {
    data object OnClickRefreshDevice : MainScreenIntent
    data class OnDeviceSelected(val device: LeoDevice) : MainScreenIntent
    data object OnClickConnect : MainScreenIntent
    object OnClickSwapWorkState : MainScreenIntent
}