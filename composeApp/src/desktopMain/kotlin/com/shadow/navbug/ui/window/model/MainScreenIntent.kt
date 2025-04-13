package com.shadow.navbug.ui.window.model

import com.shadow.navbug.domain.main.model.LeoDevice

sealed interface MainScreenIntent {
    data object OnClickRefreshDevice : MainScreenIntent
    data class OnDeviceSelected(val device: LeoDevice) : MainScreenIntent
    data object OnClickConnect : MainScreenIntent
    object OnClickSwapWorkState : MainScreenIntent
    object GetLeoConfig : MainScreenIntent

    sealed interface Config: MainScreenIntent {
        data class OnWindowCountChanged(val count: String) : Config
        data class OnKeyOneChanged(val keys: String ) : Config
        data class OnKeyTwoChanged(val keys: String ) : Config
        data class OnKeyThreeChanged(val keys: String ) : Config
        data class OnKeyFourChanged(val keys: String ) : Config
        data class OnWindowsSwapDelayChanged(val delay: String) : Config
        data class OnWindowsSpamDelayChanged(val delay: String) : Config
        data class OnDelayAfterInputChanged(val delay: String) : Config
        data class OnStatusWorkChanged(val status: Boolean) : Config
        data class OnStatusSpamChanged(val status: Boolean) : Config
        data class OnStatusSwapChanged(val status: Boolean) : Config
        data class OnKeyCodeChanged(val keyName: String) : Config
        data object ReadConfig : Config
        data object WriteConfig : Config
    }

    sealed interface RealTimeControl : MainScreenIntent {
        data object OnClickRealTimeWork : RealTimeControl
        data object OnClickRealTimeSwap : RealTimeControl
        data object OnClickRealTimeKeyPress : RealTimeControl
    }
}