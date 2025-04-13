package com.shadow.navbug.domain.main.model

data class LeoConfig(
    val windowCount: Byte = 2,
    val keys: String = "4500",
    val windowsSwapDelay: Short = 100,
    val windowsSpamDelay: Short = 50,
    val delayAfterInput: Short = 1000,
    val statusWork: Byte = 0x00,
    val statusSpam: Byte = 0x01,
    val statusSwap: Byte = 0x01,
    val keyCode: Byte = 0xC4.toByte(),
)