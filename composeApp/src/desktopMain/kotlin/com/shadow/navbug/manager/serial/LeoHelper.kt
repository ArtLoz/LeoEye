package com.shadow.navbug.manager.serial

import java.nio.ByteBuffer
import java.util.Arrays


fun ByteArray.takeShort(): Short {
    return ByteBuffer.wrap(this).short
}

fun Short.toByteArray(): ByteArray {
    val buffer = ByteBuffer.allocate(2)
    buffer.putShort(this)
    return buffer.array()
}