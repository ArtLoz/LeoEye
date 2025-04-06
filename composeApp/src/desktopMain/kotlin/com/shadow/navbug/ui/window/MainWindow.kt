package com.shadow.navbug.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.shadow.navbug.ui.base.ConnectionButton
import com.shadow.navbug.ui.base.DropdownDevice
import com.shadow.navbug.ui.window.model.MainScreenIntent
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MainWindowsApp(
    modifier: Modifier = Modifier,

    ) {
    Window(
        onCloseRequest = {}
    ) {
        val viewModel: MainViewModel = koinViewModel()
        val screenModel = viewModel.screenModel.collectAsState().value
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownDevice(
                    modifier = Modifier,
                    listDevice = screenModel.listDevice,
                    selectedDevice = screenModel.selectedDevice,
                    onClickRefresh = {
                        viewModel.receiveIntent(MainScreenIntent.OnClickRefreshDevice)
                    },
                    onDeviceChange = {
                        viewModel.receiveIntent(MainScreenIntent.OnDeviceSelected(it))
                    }
                )
                ConnectionButton(
                    modifier = Modifier.padding(start = 4.dp),
                    isConnected = screenModel.isConnected,
                    onClick = {viewModel.receiveIntent(MainScreenIntent.OnClickConnect)}
                )
            }
            Button(
                onClick = {
                    viewModel.receiveIntent(MainScreenIntent.OnClickSwapWorkState)
                }
            ) {
                Text(
                    text = "SWAP WORK_STATE"
                )
            }
        }
    }
}