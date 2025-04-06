package com.shadow.navbug.domain.main.model

import com.fazecast.jSerialComm.SerialPort

data class LeoDevice(
    val name: String,
    val statusConnection: Boolean = false,
    val baudRate: Int = 115200,
)
