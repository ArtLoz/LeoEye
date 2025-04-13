package com.shadow.navbug.ui.base

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadow.navbug.domain.main.model.LeoConfig
import com.shadow.navbug.ui.window.model.KeyPress
import com.shadow.navbug.ui.window.model.MainScreenIntent
import com.shadow.navbug.ui.window.model.UiLeoConfig
import java.time.format.TextStyle


@Composable
fun ControlUI(
    modifier: Modifier = Modifier,
    statusWork: Boolean,
    statusSwap: Boolean,
    statusSpam: Boolean,
    onAction: (MainScreenIntent) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = "Real time status control",
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            modifier = modifier.fillMaxWidth()
                .border(1.dp, color = Color.Black, shape = RoundedCornerShape(percent = 8))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ControlUi(
                name = "Work",
                status = statusWork,
                onClick = { onAction.invoke(MainScreenIntent.RealTimeControl.OnClickRealTimeWork) }
            )
            ControlUi(
                name = "Press Key",
                status = statusSpam,
                onClick = { onAction.invoke(MainScreenIntent.RealTimeControl.OnClickRealTimeKeyPress)  }
            )
            ControlUi(
                name = "Swap",
                status = statusSwap,
                onClick = { onAction.invoke(MainScreenIntent.RealTimeControl.OnClickRealTimeSwap)  }
            )
        }
    }

}

@Composable
fun ControlUi(
    modifier: Modifier = Modifier,
    name: String,
    status: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall
        )
        Button(
            modifier = Modifier.width(76.dp)
                .height(48.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (status) Color(0xFF6CA339) else Color(0xFFEF6A6A),
            ),
            shape = RoundedCornerShape(percent = 12)
        ) {
            val text = if (status) "ON" else "OFF"
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ConfigUi(
    modifier: Modifier = Modifier,
    leoConfig: UiLeoConfig,
    onConfigAction: (MainScreenIntent) -> Unit
) {
    Column {
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .padding(bottom = 4.dp),
            text = "Leo Config",
            style = MaterialTheme.typography.titleSmall
        )
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .border(1.dp, color = Color.Black, shape = RoundedCornerShape(percent = 8))
                .padding(12.dp)
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                Column(modifier = Modifier) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Windows count: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        DropdownConfig(
                            modifier = Modifier.width(56.dp),
                            listValue = leoConfig.lisWindowsCount,
                            selectedValue = leoConfig.windowCount,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnWindowCountChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Key windows 1: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        DropdownConfig(
                            modifier = Modifier.width(56.dp),
                            listValue = leoConfig.listKeyWindow,
                            selectedValue = leoConfig.keyWindowOne,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnKeyOneChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Key windows 2: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        DropdownConfig(
                            modifier = Modifier.width(56.dp),
                            listValue = leoConfig.listKeyWindow,
                            selectedValue = leoConfig.keyWindowTwo,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnKeyTwoChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Key windows 3: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        DropdownConfig(
                            modifier = Modifier.width(56.dp),
                            listValue = leoConfig.listKeyWindow,
                            selectedValue = leoConfig.keyWindowThree,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnKeyThreeChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Key windows 4: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        DropdownConfig(
                            modifier = Modifier.width(56.dp),
                            listValue = leoConfig.listKeyWindow,
                            selectedValue = leoConfig.keyWindowFour,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnKeyFourChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }

                }
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 8.dp), color = Color.Black
                )
                Column(modifier = Modifier) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "PressKey: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        DropdownConfig(
                            modifier = Modifier.width(56.dp),
                            listValue = KeyPress.entries.map {
                                it.name
                            },
                            selectedValue = leoConfig.keyPress.name,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnKeyCodeChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Win swap delay: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        ConfigNumbersTextField(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(56.dp),
                            text = leoConfig.windowsSwapDelay,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnWindowsSwapDelayChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Key press delay: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        ConfigNumbersTextField(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(56.dp),
                            text = leoConfig.windowsSpamDelay,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnWindowsSpamDelayChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Input reaction delay: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                        )
                        ConfigNumbersTextField(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(56.dp),
                            text = leoConfig.delayAfterInput,
                            onValueChange = {
                                onConfigAction.invoke(
                                    MainScreenIntent.Config.OnDelayAfterInputChanged(
                                        it
                                    )
                                )
                            },
                        )
                    }

                    Column(modifier = Modifier.height(40.dp)) {
                        Text(
                            text = "Default Status:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp
                            )
                        )
                        Row(
                            modifier = Modifier
                                .padding(start = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "Work:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                            )
                            CustomCheckBox(
                                modifier = Modifier.padding(top = 4.dp),
                                check = leoConfig.statusWork,
                                onCheckChange = {
                                    onConfigAction.invoke(
                                        MainScreenIntent.Config.OnStatusWorkChanged(
                                            it
                                        )
                                    )
                                },
                            )
                            VerticalDivider(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .padding(horizontal = 4.dp),
                                color = Color.Black
                            )
                            Text(
                                modifier = Modifier,
                                text = "Swap: ",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                            )
                            CustomCheckBox(
                                modifier = Modifier.padding(top = 4.dp),
                                check = leoConfig.statusSwap,
                                onCheckChange = {
                                    onConfigAction.invoke(
                                        MainScreenIntent.Config.OnStatusSwapChanged(
                                            it
                                        )
                                    )
                                },
                            )
                            VerticalDivider(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .padding(horizontal = 4.dp),
                                color = Color.Black
                            )
                            Text(
                                modifier = Modifier,
                                text = "Press: ",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                            )
                            CustomCheckBox(
                                modifier = Modifier.padding(top = 4.dp),
                                check = leoConfig.statusSpam,
                                onCheckChange = {
                                    onConfigAction.invoke(
                                        MainScreenIntent.Config.OnStatusSpamChanged(
                                            it
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { onConfigAction.invoke(MainScreenIntent.Config.ReadConfig) },
                    shape = RoundedCornerShape(percent = 12),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text(
                        text = "Read Config",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Button(
                    onClick = { onConfigAction.invoke(MainScreenIntent.Config.WriteConfig) },
                    shape = RoundedCornerShape(percent = 12),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text(
                        text = "Save Config",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

    }

}

@Composable
fun CustomCheckBox(
    modifier: Modifier = Modifier,
    check: Boolean,
    onCheckChange: (Boolean) -> Unit,
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(percent = 8))
            .clickable { onCheckChange.invoke(!check) },
        contentAlignment = Alignment.Center
    ) {
        if (check) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
            )
        }
    }
}