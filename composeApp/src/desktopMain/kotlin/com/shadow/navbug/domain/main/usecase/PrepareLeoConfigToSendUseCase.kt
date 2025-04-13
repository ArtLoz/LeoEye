package com.shadow.navbug.domain.main.usecase

import com.shadow.navbug.domain.main.model.LeoConfig
import com.shadow.navbug.manager.serial.UPDATE_CONFIG
import com.shadow.navbug.manager.serial.createCheckSumPart
import com.shadow.navbug.manager.serial.toByteArray
import com.shadow.navbug.manager.serial.toHex
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PrepareLeoConfigToSendUseCase() {

    operator fun invoke(
        config: LeoConfig
    ): ByteArray {
        val commandByteArray = UPDATE_CONFIG.toByteArray()
        val payload = createByteArrayFormLeoConfig(config)

        val crc = createCheckSumPart(payload)
        return commandByteArray + payload + crc
    }

    private fun createByteArrayFormLeoConfig(config: LeoConfig): ByteArray {
        val reservedBytes = ByteArray(25)
        val keysBytes = config.keys
            .take(4)
            .map { it.code.toByte() }
            .toByteArray()
        print(config.keyCode)
        return ByteBuffer.allocate(40)
            .order(ByteOrder.LITTLE_ENDIAN)
            .put(config.windowCount)
            .put(keysBytes)
            .put(config.keyCode)
            .putShort(config.windowsSwapDelay)
            .putShort(config.windowsSpamDelay.toShort())
            .putShort(config.delayAfterInput.toShort())
            .put(config.statusWork)
            .put(config.statusSwap)
            .put(config.statusSpam)
            .put(reservedBytes)
            .array()
    }
}