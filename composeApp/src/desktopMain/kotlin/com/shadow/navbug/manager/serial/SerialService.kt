package com.shadow.navbug.manager.serial

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import com.shadow.navbug.domain.main.model.LeoDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.jvm.Throws

class SerialService {

    private var pollingJob: Job? = null
    private var currentConnectionDevice: SerialPort? = null
    private var deviceListener: SerialPortDataListener? = null

    private val _inputDataFlow = MutableSharedFlow<SerialData>(extraBufferCapacity = 1)
    val inputDataFlow = _inputDataFlow.asSharedFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()


    fun getAvailablePorts(): List<LeoDevice> {
        val ports = SerialPort.getCommPorts().filter {
            it.portDescription == PORT_DES
        }.map {
            LeoDevice(
                name = it.systemPortName,
                statusConnection = false,
                baudRate = 115200,
            )
        }
        return ports
    }

    @Throws
    fun connectionToDevice(portName: String, baudRate: Int = 9600) {
        val serialPort = configureSerialPort(portName, baudRate)
            ?: throw Exception("Error configuring port: $portName")
        if (!serialPort.openPort()) {
            throw Exception("Error opening port: $portName")
        }
        currentConnectionDevice = serialPort

        val buffer = ByteArray(1024)
        val deviceListener = createDataListener(serialPort, buffer)
        serialPort.addDataListener(deviceListener)
        statusDevice()
    }

    private fun configureSerialPort(portName: String, baudRate: Int): SerialPort? {
        return try {
            SerialPort.getCommPort(portName).apply {
                this.baudRate = baudRate
                setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createDataListener(
        serialPort: SerialPort,
        buffer: ByteArray
    ): SerialPortDataListener {
        return object : SerialPortDataListener {
            override fun getListeningEvents(): Int = SerialPort.LISTENING_EVENT_DATA_AVAILABLE

            override fun serialEvent(event: SerialPortEvent?) {
                if (event?.eventType != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return
                val available = serialPort.bytesAvailable()
                if (available <= 0) return
                val bytesRead = serialPort.readBytes(buffer, available)
                if (bytesRead > 0) {
                    try {
                        val data = buffer.copyOf(bytesRead)
                        if(data.size < 2) return
                        val command = data.copyOfRange(0, 2)
                        if (command.takeShortBE() == POLLING) statusDevice()
                        val payload = if(data .size > 2) {
                            data.copyOfRange(2, bytesRead)
                        } else { ByteArray(0) }
                        _inputDataFlow.tryEmit(SerialData(command, payload))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun statusDevice() {
        _isConnected.value = true
        pollingJob?.cancel()
        pollingJob = CoroutineScope(Dispatchers.IO).launch {
            delay(5000)
            _isConnected.value = false
            disconnect()
        }
    }

    fun writeData(data: ByteArray) {
        if(isConnected.value) currentConnectionDevice?.writeBytes(data, data.size)
    }

    fun disconnect() {
        currentConnectionDevice?.let {
            it.closePort()
            it.removeDataListener()

        }
        deviceListener = null
        currentConnectionDevice = null
        _isConnected.value = false
    }


    private companion object {
        const val PORT_DES = "Arduino Leonardo"
    }

    class SerialData(val command: ByteArray, val payload: ByteArray)

}
