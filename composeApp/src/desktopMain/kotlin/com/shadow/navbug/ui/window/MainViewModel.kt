package com.shadow.navbug.ui.window

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.navbug.domain.main.model.LeoDevice
import com.shadow.navbug.manager.jnativehook.JNativeHookManager
import com.shadow.navbug.manager.jnativehook.listener.mouse.GlobalListenerEvent
import com.shadow.navbug.manager.serial.DISABLE_SWAP_WINDOWS
import com.shadow.navbug.manager.serial.SWAP_WORK_STATE
import com.shadow.navbug.manager.serial.SerialService
import com.shadow.navbug.manager.serial.toByteArray
import com.shadow.navbug.ui.base.io
import com.shadow.navbug.ui.window.model.MainScreenIntent
import com.shadow.navbug.ui.window.model.MainScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val serialService: SerialService,
    private val jNativeHookManager: JNativeHookManager
) : ViewModel() {

    private val _screenModel = MutableStateFlow(MainScreenModel())
    val screenModel = _screenModel.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            serialService.inputDataFlow.collect {  data->
                println        (
                    data.command.joinToString()
                )
                println(
                    data.payload.joinToString()
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            serialService.isConnected.collect { status ->
                _screenModel.update {
                    it.copy(
                        isConnected = status
                    )
                }
            }
        }
        io{
            jNativeHookManager.globalEventFlow.collect {
                if(_screenModel.value.isConnected){
                    when(it){
                        is GlobalListenerEvent.KeyboardEvent -> {
                            if(it.keyCode in _screenModel.value.ignoreKeys) return@collect
                            serialService.writeData(DISABLE_SWAP_WINDOWS.toByteArray())
                        }
                        else -> {
                            serialService.writeData(DISABLE_SWAP_WINDOWS.toByteArray())
                        }
                    }
                }
            }
        }
    }


    fun receiveIntent(intent: MainScreenIntent) {
        when (intent) {
            MainScreenIntent.OnClickRefreshDevice -> refreshDevice()
            is MainScreenIntent.OnDeviceSelected -> selectDevice(intent.device)
            MainScreenIntent.OnClickConnect -> connectionToDevice()
            MainScreenIntent.OnClickSwapWorkState -> sendToDevice(SWAP_WORK_STATE.toByteArray())
        }
    }

    private fun refreshDevice() {
        val ports = serialService.getAvailablePorts()
        _screenModel.update {
            it.copy(
                listDevice = ports
            )
        }
    }

    private fun selectDevice(device: LeoDevice) {
        _screenModel.update {
            it.copy(
                selectedDevice = device
            )
        }
    }

    private fun connectionToDevice() {
        try {
            val device = screenModel.value.selectedDevice
            if (device != null) {
                serialService.connectionToDevice(portName = device.name, 115200)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun sendToDevice(data: ByteArray){
        if(screenModel.value.isConnected) serialService.writeData(data)
    }
}