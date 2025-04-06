package com.shadow.navbug.ui.base

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shadow.navbug.domain.main.model.LeoDevice

@Composable
fun DropdownDevice(
    modifier: Modifier = Modifier,
    listDevice: List<LeoDevice>,
    selectedDevice: LeoDevice?,
    onClickRefresh: () -> Unit,
    onDeviceChange: (LeoDevice) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable {
                    onClickRefresh()
                    expanded = true
                }
                .width(225.dp)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(percent = 15))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Device:",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = selectedDevice?.name?:"",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listDevice.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onDeviceChange(item)
                        expanded = false
                    },
                    text = { Text(text = item.name) }
                )
            }
        }
    }
}