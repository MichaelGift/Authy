package com.myth.authy.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DataPointComponent(
    key: String,
    onValueChange: (String) -> Unit,
    value: String,
    number: Boolean = false
) {
    var readOnly by remember { mutableStateOf(true) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(key) },
        singleLine = true,
        readOnly = readOnly,
        trailingIcon = {
            val image =
                if (readOnly) Icons.Default.Edit else Icons.Default.Lock

            val description = if (readOnly) "Editing" else "Locked"
            IconButton(onClick = { readOnly = !readOnly }) {
                Icon(image, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (number) KeyboardType.Number else KeyboardType.Text
        )
    )
}
