package com.shadow.navbug.manager.jnativehook

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.shadow.navbug.manager.jnativehook.listener.GlobalListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class JNativeHookManager(
    private val globalListener: GlobalListener
) {

    val globalEventFlow = globalListener.globalListenerEventFlow

    init {
        registerNativeHook()
    }

    private fun registerNativeHook() {
        try {
            GlobalScreen.registerNativeHook()
            if (GlobalScreen.isNativeHookRegistered()) {
                addListener()
            }
        } catch (e: NativeHookException) {
            println("registerNativeHook error ${GlobalScreen.isNativeHookRegistered()}")
            removeListener()
        }
    }

    private fun addListener() {
        GlobalScreen.addNativeMouseListener(globalListener.nativeMouseInputListener)
        GlobalScreen.addNativeMouseMotionListener(globalListener.nativeMouseInputListener)
        GlobalScreen.addNativeMouseWheelListener(globalListener.nativeMouseWheelListener)
        GlobalScreen.addNativeKeyListener(globalListener.nativeKeyListener)

    }

    private fun removeListener() {
        GlobalScreen.removeNativeMouseListener(globalListener.nativeMouseInputListener)
        GlobalScreen.removeNativeMouseMotionListener(globalListener.nativeMouseInputListener)
        GlobalScreen.removeNativeMouseWheelListener(globalListener.nativeMouseWheelListener)
        GlobalScreen.removeNativeKeyListener(globalListener.nativeKeyListener)
    }
}