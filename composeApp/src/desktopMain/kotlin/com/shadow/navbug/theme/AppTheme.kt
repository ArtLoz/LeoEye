package com.shadow.navbug.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.koin.compose.KoinContext


@Composable
fun init(content: @Composable () -> Unit) {
    KoinContext {
        AppTheme {
            content()
        }
    }
}
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme{
        content()
    }
}
