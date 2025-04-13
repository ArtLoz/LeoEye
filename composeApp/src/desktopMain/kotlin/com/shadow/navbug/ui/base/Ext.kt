package com.shadow.navbug.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.navbug.domain.main.model.LeoConfig
import com.shadow.navbug.manager.serial.toByte
import com.shadow.navbug.ui.window.model.KeyPress
import com.shadow.navbug.ui.window.model.UiLeoConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun ViewModel.io(crossinline suspender: suspend () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        suspender()
    }
}

fun UiLeoConfig.toLeoConfig(): LeoConfig {
    return LeoConfig(
        windowCount = this.windowCount.toByte(),
        keys = this.keyWindowOne + this.keyWindowTwo + this.keyWindowThree + this.keyWindowFour,
        windowsSpamDelay = this.windowsSpamDelay.toShort(),
        windowsSwapDelay = this.windowsSwapDelay.toShort(),
        delayAfterInput = this.delayAfterInput.toShort(),
        statusSpam = this.statusSpam.toByte(),
        statusSwap = this.statusSwap.toByte(),
        statusWork = this.statusWork.toByte(),
        keyCode = this.keyPress.code
    )
}

fun LeoConfig.toUiLeoConfig(): UiLeoConfig {
    return UiLeoConfig(
        windowCount = this.windowCount.toString(),
        keyWindowOne = this.keys[0].toString(),
        keyWindowTwo = this.keys[1].toString(),
        keyWindowThree = this.keys[2].toString(),
        keyWindowFour = this.keys[3].toString(),
        windowsSwapDelay = this.windowsSwapDelay.toString(),
        windowsSpamDelay = this.windowsSpamDelay.toString(),
        delayAfterInput = this.delayAfterInput.toString(),
        statusSpam = this.statusSpam == 0x01.toByte(),
        statusSwap = this.statusSwap == 0x01.toByte(),
        statusWork = this.statusWork == 0x01.toByte(),
        keyPress = KeyPress.entries.find { it.code == this.keyCode } ?: KeyPress.F9
    )
}