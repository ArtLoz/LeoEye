package com.shadow.navbug.ui.window.model

data class UiLeoConfig(
    val windowCount: String = "2",
    val lisWindowsCount: List<String> = listOf("1", "2", "3", "4"),
    val listKeyWindow: List<String> = listOf("0","1", "2", "3", "4", "5", "6", "7", "8","9"),
    val keyWindowOne: String = "1",
    val keyWindowTwo: String = "2",
    val keyWindowThree: String = "3",
    val keyWindowFour: String = "4",
    val windowsSwapDelay: String = "100",
    val windowsSpamDelay: String = "50",
    val delayAfterInput: String = "1000",
    val statusWork: Boolean = false,
    val statusSpam: Boolean = false,
    val statusSwap: Boolean = false,
    val keyPress: KeyPress = KeyPress.F1
)