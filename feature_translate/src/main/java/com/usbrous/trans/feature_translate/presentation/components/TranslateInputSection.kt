package com.usbrous.trans.feature_translate.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

private const val MAX_INPUT_LENGTH = 5000

@Composable
fun TranslateInputSection(
    inputText: String,
    isTranslating: Boolean,
    onInputTextChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    onClearClick: () -> Unit,
    onPasteClick: () -> Unit,
    onCopyInputClick: () -> Unit,
    onMicClick: () -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isNearLimit = inputText.length > (MAX_INPUT_LENGTH * 0.9).toInt()
    val isAtLimit = inputText.length >= MAX_INPUT_LENGTH

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            ) {
                if (inputText.isEmpty()) {
                    Text(
                        text = "Enter text, paste, use mic, or camera",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }

                BasicTextField(
                    value = inputText,
                    onValueChange = { newText ->
                        if (newText.length <= MAX_INPUT_LENGTH) {
                            onInputTextChange(newText)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    maxLines = 8
                )
            }

            if (inputText.isNotEmpty()) {
                Text(
                    text = "${inputText.length} / $MAX_INPUT_LENGTH",
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        isAtLimit -> MaterialTheme.colorScheme.error
                        isNearLimit -> MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onMicClick) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice input",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onCameraClick) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera input",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (inputText.isEmpty()) {
                        IconButton(onClick = onPasteClick) {
                            Icon(
                                imageVector = Icons.Default.ContentPaste,
                                contentDescription = "Paste text",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    if (inputText.isNotEmpty()) {
                        IconButton(onClick = onCopyInputClick) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy input text",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (inputText.isNotEmpty()) {
                        IconButton(onClick = onClearClick) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear text",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        FilledIconButton(
                            onClick = onTranslateClick,
                            enabled = !isTranslating,
                            modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Translate",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
