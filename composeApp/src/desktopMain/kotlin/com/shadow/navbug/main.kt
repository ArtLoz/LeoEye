package com.shadow.navbug

import androidx.compose.ui.window.application
import com.shadow.navbug.di.initKoin
import com.shadow.navbug.theme.init


fun main(){
    initKoin()
    application {
        init {

        }
    }
}
