package com.shadow.navbug.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            serviceModules, viewModelsModule, jNativeHookModule, useCase
        )
    }
}