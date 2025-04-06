package com.shadow.navbug.manager.jnativehook.listener

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.shadow.navbug.manager.jnativehook.listener.mouse.GlobalListenerEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GlobalListener {


    private val _globalListenerEventFlow = MutableSharedFlow<GlobalListenerEvent>(replay = 1)
    val globalListenerEventFlow = _globalListenerEventFlow.asSharedFlow()

    val nativeMouseInputListener = JNativeMouseInputListener()
    val nativeMouseWheelListener = JNativeMouseWheelListener()
    val nativeKeyListener = JNativeKeyListener()
    private var lastMouseMovedTime = 0L
    private val mutex = Mutex()


    inner class JNativeMouseInputListener : NativeMouseInputListener {

        override fun nativeMouseReleased(nativeEvent: NativeMouseEvent?) {
            super.nativeMouseReleased(nativeEvent)

            _globalListenerEventFlow.tryEmit(GlobalListenerEvent.MouseReleased)
        }

        @OptIn(DelicateCoroutinesApi::class)
        override fun nativeMouseMoved(nativeEvent: NativeMouseEvent?) {
            super.nativeMouseMoved(nativeEvent)
            CoroutineScope(Dispatchers.IO).launch {
                mutex.withLock {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastMouseMovedTime > 100) {
                        _globalListenerEventFlow.tryEmit(GlobalListenerEvent.MouseMoved)
                        lastMouseMovedTime = currentTime
                    }
                }
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        override fun nativeMouseDragged(nativeEvent: NativeMouseEvent?) {
            super.nativeMouseDragged(nativeEvent)
            _globalListenerEventFlow.tryEmit(GlobalListenerEvent.MouseDragged)
        }
    }

    inner class JNativeMouseWheelListener : NativeMouseWheelListener {
        @OptIn(DelicateCoroutinesApi::class)
        override fun nativeMouseWheelMoved(nativeEvent: NativeMouseWheelEvent?) {
            super.nativeMouseWheelMoved(nativeEvent)
            _globalListenerEventFlow.tryEmit(GlobalListenerEvent.MouseWheelMoved)

        }

    }

    inner class JNativeKeyListener : NativeKeyListener {
        override fun nativeKeyReleased(nativeEvent: NativeKeyEvent?) {
            super.nativeKeyReleased(nativeEvent)
            println("key released: ${nativeEvent?.keyCode}")
            _globalListenerEventFlow.tryEmit(GlobalListenerEvent.KeyboardEvent(keyCode = nativeEvent?.keyCode ?: -1))
        }
    }
}