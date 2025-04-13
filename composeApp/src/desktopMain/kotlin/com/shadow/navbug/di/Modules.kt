package com.shadow.navbug.di

import com.shadow.navbug.domain.main.usecase.ParseConfigFromByteArrayUseCase
import com.shadow.navbug.domain.main.usecase.PrepareLeoConfigToSendUseCase
import com.shadow.navbug.manager.jnativehook.JNativeHookManager
import com.shadow.navbug.manager.jnativehook.listener.GlobalListener
import com.shadow.navbug.manager.serial.SerialService
import com.shadow.navbug.ui.window.MainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val serviceModules = module{
    singleOf(::SerialService)
}

val viewModelsModule = module {
    viewModelOf(::MainViewModel)
}
val jNativeHookModule = module {
    singleOf(::JNativeHookManager)
    singleOf(::GlobalListener)
}

val useCase = module {
    factoryOf(::PrepareLeoConfigToSendUseCase)
    factoryOf(::ParseConfigFromByteArrayUseCase)
}