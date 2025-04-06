package com.shadow.navbug

import androidx.compose.ui.window.application
import com.shadow.navbug.di.initKoin
import com.shadow.navbug.manager.serial.SerialService
import com.shadow.navbug.ui.theme.init
import androidx.compose.ui.window.Window
import com.shadow.navbug.ui.window.MainWindowsApp


fun main(){
    initKoin()
    application {
        init {
            MainWindowsApp()
        }
    }
}
