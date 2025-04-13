package com.shadow.navbug.manager.serial

class CRC16Mod {
    var value: Int = 0xFFFF
        private set

    fun update(aByte: Byte) {
        var byte = aByte.toInt() and 0xFF
        value = value xor byte
        for (i in 0 until 8) {
            value = if ((value and 0x0001) != 0) {
                (value ushr 1) xor 0xA001
            } else {
                value ushr 1
            }
        }
        value = value and 0xFFFF
    }

    fun reset() {
        value = 0xFFFF
    }
}
