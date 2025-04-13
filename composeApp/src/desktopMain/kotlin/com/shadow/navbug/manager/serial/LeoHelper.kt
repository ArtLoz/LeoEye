package com.shadow.navbug.manager.serial

import java.nio.ByteBuffer
import java.nio.ByteOrder


fun ByteArray.takeShort(): Short {
    return ByteBuffer.wrap(this)
        .order(ByteOrder.LITTLE_ENDIAN)
        .short
}

fun ByteArray.takeShortBE(): Short {
    return ByteBuffer.wrap(this)
        .order(ByteOrder.BIG_ENDIAN)
        .short
}

fun Short.toByteArray(): ByteArray {
    val buffer = ByteBuffer.allocate(2)
    buffer.putShort(this)
    return buffer.array()
}

fun createCheckSumPart(bytes: ByteArray): ByteArray {
    val crc16 = CRC16Mod()
    bytes.forEach {
        crc16.update(it)
    }
    return ByteBuffer.allocate(2).putShort(crc16.value.toShort()).array()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

fun Boolean.toByte(): Byte {
    return if (this) 0x01 else 0x00
}
fun Byte.toBoolean(): Boolean {
    return this == 0x01.toByte()
}