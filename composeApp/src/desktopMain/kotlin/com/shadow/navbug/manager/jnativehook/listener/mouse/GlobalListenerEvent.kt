package com.shadow.navbug.manager.jnativehook.listener.mouse

sealed class GlobalListenerEvent {
    data object MouseReleased : GlobalListenerEvent()
    data object MouseMoved : GlobalListenerEvent()
    data object MouseDragged : GlobalListenerEvent()
    data object MouseWheelMoved : GlobalListenerEvent()
    data class KeyboardEvent(val keyCode: Int) : GlobalListenerEvent()
}