package com.shadow.navbug.ui.window.model

import com.shadow.navbug.domain.main.model.LeoDevice

data class MainScreenModel(
    val listDevice: List<LeoDevice> = emptyList(),
    val selectedDevice: LeoDevice? = null,
    val isConnected: Boolean = false,
    val ignoreKeys: List<Int> = listOf(61, 6, 5, 3675)
)