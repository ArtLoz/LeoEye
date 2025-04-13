package com.shadow.navbug.domain.main.usecase

import com.shadow.navbug.domain.main.model.LeoConfig
import com.shadow.navbug.manager.serial.createCheckSumPart
import com.shadow.navbug.manager.serial.takeShort
import com.shadow.navbug.manager.serial.toHex
import kotlin.jvm.Throws


class ParseConfigFromByteArrayUseCase() {

    @Throws
    operator fun invoke(inputData: ByteArray) : LeoConfig {
        if(inputData.size  < 40) {
            throw IllegalArgumentException("Input data size is less than 40 bytes")
        }
        val crc = inputData.sliceArray(inputData.size - 2 until inputData.size)
        val payload = inputData.sliceArray(0 until inputData.size - 2)
        val payloadCrc = createCheckSumPart(payload)
        if(!payloadCrc.contentEquals(crc)) {
            throw IllegalArgumentException("Invalid CRC")
        }
        val windowCount = payload[0]
        val keys = payload.sliceArray(1..4).map { it.toChar() }.joinToString("")
        val keyCode = payload[5]
        val windowsSwapDelay = payload.sliceArray(6..7).takeShort()
        val windowsSpamDelay = payload.sliceArray(8..9).takeShort()
        val delayAfterInput = payload.sliceArray(10..11).takeShort()
        val statusWork = payload[12]
        val statusSwap = payload[13]
        val statusSpam = payload[14]
        val config = LeoConfig(
            windowCount = windowCount,
            keys = keys,
            windowsSwapDelay = windowsSwapDelay,
            windowsSpamDelay = windowsSpamDelay,
            delayAfterInput = delayAfterInput,
            statusWork = statusWork,
            statusSpam = statusSpam,
            statusSwap = statusSwap,
            keyCode = keyCode
        )
        return config
    }
}