package com.usbrous.trans.feature_translate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.usbrous.trans.core.ui.BrandAmber
import com.usbrous.trans.core.ui.BrandMagenta
import com.usbrous.trans.core.ui.BrandPurple

private const val MAX_INPUT_LENGTH = 5000

@Composable
fun TranslateInputSection(
    inputText: String,
    isTranslating: Boolean,
    onInputTextChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    onClearClick: () -> Unit,
    onPasteClick: () -> Unit,
    onMicClick: () -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── Input text area ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                if (inputText.isEmpty()) {
                    Text(
                        text = "Tap to enter a text",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                    )
                }

                BasicTextField(
                    value = inputText,
                    onValueChange = { newText ->
                        if (newText.length <= MAX_INPUT_LENGTH) onInputTextChange(newText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        lineHeight = MaterialTheme.typography.headlineSmall.lineHeight
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }

            // ── Character count (shown near limit) ─────────────────────────
            val isNearLimit = inputText.length > (MAX_INPUT_LENGTH * 0.9).toInt()
            if (isNearLimit) {
                Text(
                    text = "${inputText.length} / $MAX_INPUT_LENGTH",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (inputText.length >= MAX_INPUT_LENGTH)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp, bottom = 4.dp)
                )
            }

            // ── Action buttons row ──────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Voice, Camera, Paste
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleIconButton(
                        icon = Icons.Default.Mic,
                        label = "Voice",
                        contentDescription = "Voice input",
                        backgroundColor = BrandPurple,
                        iconTint = Color.White,
                        onClick = onMicClick
                    )
                    CircleIconButton(
                        icon = Icons.Default.CameraAlt,
                        label = "Camera",
                        contentDescription = "Camera input",
                        backgroundColor = BrandMagenta,
                        iconTint = Color.White,
                        onClick = onCameraClick
                    )
                    // Paste – no filled circle background
                    FlatIconButton(
                        icon = Icons.Default.ContentPaste,
                        label = "Paste",
                        contentDescription = "Paste text",
                        tint = MaterialTheme.colorScheme.onSurface,
                        onClick = onPasteClick
                    )
                }

                // Right: Keyboard, Translate
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleIconButton(
                        icon = Icons.Default.Keyboard,
                        label = "Keyboard",
                        contentDescription = "Show keyboard",
                        backgroundColor = BrandPurple,
                        iconTint = Color.White,
                        onClick = {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                    )
                    CircleIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        label = "Translate",
                        contentDescription = "Translate",
                        backgroundColor = BrandAmber,
                        iconTint = Color.Black,
                        onClick = onTranslateClick,
                        enabled = !isTranslating
                    )
                }
            }
        }
    }
}

// ── Shared button composables ────────────────────────────────────────────────

@Composable
private fun CircleIconButton(
    icon: ImageVector,
    label: String,
    contentDescription: String,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    if (enabled) backgroundColor
                    else backgroundColor.copy(alpha = 0.4f)
                )
                .clickable(enabled = enabled, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun FlatIconButton(
    icon: ImageVector,
    label: String,
    contentDescription: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
