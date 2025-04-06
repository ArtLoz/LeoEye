package com.shadow.navbug.ui.base

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun ConnectionButton(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if(isConnected) Color.Green else Color.Gray,
        )
    ) {
        Text(
            text = if (isConnected) "Disconnect" else "Connect",
        )
    }
}