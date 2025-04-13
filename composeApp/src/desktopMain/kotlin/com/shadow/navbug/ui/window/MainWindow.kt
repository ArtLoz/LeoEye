package com.shadow.navbug.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import com.shadow.navbug.ui.base.ConfigUi
import com.shadow.navbug.ui.base.ConnectionButton
import com.shadow.navbug.ui.base.ControlUI
import com.shadow.navbug.ui.base.ControlUi
import com.shadow.navbug.ui.base.DropdownConfig
import com.shadow.navbug.ui.base.DropdownDevice
import com.shadow.navbug.ui.window.model.MainScreenIntent
import org.koin.compose.viewmodel.koinViewModel
import kotlin.system.exitProcess


@Composable
fun MainWindowsApp(
    modifier: Modifier = Modifier,

    ) {
    Window(
        onCloseRequest = { exitProcess(-1) },
        title = "Leo Eye",
        state =WindowState(
            width = 466.dp,
            height = 514.dp
        ),
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
                    modifier = Modifier.padding(start = 32.dp),
                    isConnected = screenModel.isConnected,
                    onClick = {viewModel.receiveIntent(MainScreenIntent.OnClickConnect)}
                )
            }
            ControlUI(
                modifier = Modifier.padding(vertical = 8.dp),
                statusSpam = screenModel.realtimeKeyPress,
                statusSwap = screenModel.realTimeSwap,
                statusWork = screenModel.realTimeWork,
                onAction = viewModel::receiveIntent
            )
            ConfigUi(
                modifier = Modifier,
                leoConfig = screenModel.leoConfig,
                onConfigAction = viewModel::receiveIntent
            )
        }
    }
}