package com.shadow.navbug.ui.base

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ConfigNumbersTextField(
    modifier:Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
){
    BasicTextField(
        value = text,
        onValueChange = { newText ->
            if (newText.toIntOrNull() != null || newText.isEmpty()) {
                onValueChange.invoke(newText)
            }
        },
        modifier = modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(4.dp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        decorationBox = { innerTextField ->
            innerTextField()
        }
    )
}