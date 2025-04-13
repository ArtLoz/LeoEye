package com.shadow.navbug.ui.window

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.navbug.domain.main.model.LeoDevice
import com.shadow.navbug.domain.main.usecase.ParseConfigFromByteArrayUseCase
import com.shadow.navbug.domain.main.usecase.PrepareLeoConfigToSendUseCase
import com.shadow.navbug.manager.jnativehook.JNativeHookManager
import com.shadow.navbug.manager.jnativehook.listener.mouse.GlobalListenerEvent
import com.shadow.navbug.manager.serial.DISABLE_SWAP_WINDOWS_INPUT
import com.shadow.navbug.manager.serial.GET_CONFIG
import com.shadow.navbug.manager.serial.POLLING
import com.shadow.navbug.manager.serial.SWAP_PRESS_KEY
import com.shadow.navbug.manager.serial.SWAP_SWAP_WINDOW_KEY
import com.shadow.navbug.manager.serial.SWAP_WORK_STATE
import com.shadow.navbug.manager.serial.SerialService
import com.shadow.navbug.manager.serial.takeShortBE
import com.shadow.navbug.manager.serial.toBoolean
import com.shadow.navbug.manager.serial.toByteArray
import com.shadow.navbug.manager.serial.toHex
import com.shadow.navbug.ui.base.io
import com.shadow.navbug.ui.base.toLeoConfig
import com.shadow.navbug.ui.base.toUiLeoConfig
import com.shadow.navbug.ui.window.model.KeyPress
import com.shadow.navbug.ui.window.model.MainScreenIntent
import com.shadow.navbug.ui.window.model.MainScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val serialService: SerialService,
    private val jNativeHookManager: JNativeHookManager,
    private val parseLeoConfigToSendUseCase: PrepareLeoConfigToSendUseCase,
    private val parseConfigFromByteArrayUseCase: ParseConfigFromByteArrayUseCase
) : ViewModel() {

    private val _screenModel = MutableStateFlow(MainScreenModel())
    val screenModel = _screenModel.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            serialService.inputDataFlow.collect { data ->
                when (data.command.takeShortBE()) {
                    GET_CONFIG -> leoConfigParse(data.payload)
                    POLLING -> parsePolingInfo(data.payload)
                    else -> Unit
                }
            }
        }
        io {
            serialService.isConnected.collect { status ->
                _screenModel.update {
                    it.copy(
                        isConnected = status
                    )
                }
                if (status) {
                    sendToDevice(GET_CONFIG.toByteArray())
                }
            }
        }
        io {
            jNativeHookManager.globalEventFlow.collect {
                if (_screenModel.value.isConnected) {
                    when (it) {
                        is GlobalListenerEvent.KeyboardEvent -> {
                            if (it.keyCode in _screenModel.value.ignoreKeys) return@collect
                           // sendToDevice(DISABLE_SWAP_WINDOWS_INPUT.toByteArray())
                        }

                        else -> {
                               serialService.writeData(DISABLE_SWAP_WINDOWS_INPUT.toByteArray())
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
            MainScreenIntent.OnClickConnect -> swapConnectionState()
            MainScreenIntent.OnClickSwapWorkState -> sendToDevice(SWAP_WORK_STATE.toByteArray())
            MainScreenIntent.GetLeoConfig -> sendToDevice(GET_CONFIG.toByteArray())
            is MainScreenIntent.Config -> receiveConfigIntent(intent)
            is MainScreenIntent.RealTimeControl -> receiveRealTimeControl(intent)
        }
    }

    private fun receiveConfigIntent(intent: MainScreenIntent.Config) {
        when (intent) {
            is MainScreenIntent.Config.OnDelayAfterInputChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            delayAfterInput = intent.delay
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnKeyCodeChanged -> {
                val findKey = KeyPress.valueOf(intent.keyName)
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            keyPress = findKey
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnKeyFourChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            keyWindowFour = intent.keys
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnKeyOneChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            keyWindowOne = intent.keys
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnKeyThreeChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            keyWindowThree = intent.keys
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnKeyTwoChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            keyWindowTwo = intent.keys
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnStatusSpamChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            statusSpam = intent.status
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnStatusSwapChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            statusSwap = intent.status
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnStatusWorkChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            statusWork = intent.status
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnWindowCountChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            windowCount = intent.count
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnWindowsSpamDelayChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            windowsSpamDelay = intent.delay
                        )
                    )
                }
            }

            is MainScreenIntent.Config.OnWindowsSwapDelayChanged -> {
                _screenModel.update {
                    it.copy(
                        leoConfig = it.leoConfig.copy(
                            windowsSwapDelay = intent.delay
                        )
                    )
                }
            }

            MainScreenIntent.Config.ReadConfig -> sendToDevice(GET_CONFIG.toByteArray())
            MainScreenIntent.Config.WriteConfig -> uploadLeoConfig()
        }
    }

    private fun receiveRealTimeControl(intent: MainScreenIntent.RealTimeControl) {
        when (intent) {
            MainScreenIntent.RealTimeControl.OnClickRealTimeKeyPress -> sendToDevice(SWAP_PRESS_KEY.toByteArray())
            MainScreenIntent.RealTimeControl.OnClickRealTimeSwap -> sendToDevice(
                SWAP_SWAP_WINDOW_KEY.toByteArray()
            )

            MainScreenIntent.RealTimeControl.OnClickRealTimeWork -> sendToDevice(SWAP_WORK_STATE.toByteArray())
        }
    }

    private fun parsePolingInfo(data: ByteArray) {
        try {
            if (data.size < 3) return
            _screenModel.update {
                it.copy(
                    realTimeWork = data[0].toBoolean(),
                    realTimeSwap = data[1].toBoolean(),
                    realtimeKeyPress = data[2].toBoolean(),
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    private fun swapConnectionState() {
        try {
            if (_screenModel.value.isConnected) {
                serialService.disconnect()
            } else {
                val device = screenModel.value.selectedDevice
                if (device != null) {
                    serialService.connectionToDevice(portName = device.name, 115200)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun leoConfigParse(data: ByteArray) {
        try {
            val leoConfig = parseConfigFromByteArrayUseCase(data)
            _screenModel.update {
                it.copy(
                    leoConfig = leoConfig.toUiLeoConfig(),
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun uploadLeoConfig() {
        val leoConfig = screenModel.value.leoConfig.toLeoConfig()
        val byteArray = parseLeoConfigToSendUseCase(leoConfig)
        sendToDevice(byteArray)

    }

    private fun sendToDevice(data: ByteArray) {
        serialService.writeData(data)
    }
}