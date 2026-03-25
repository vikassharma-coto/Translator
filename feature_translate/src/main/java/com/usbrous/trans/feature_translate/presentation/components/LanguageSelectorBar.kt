package com.usbrous.trans.feature_translate.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.usbrous.trans.core.ui.BrandPurple
import com.usbrous.trans.feature_translate.domain.model.Language

@Composable
fun LanguageSelectorBar(
    sourceLanguage: Language,
    targetLanguage: Language,
    onSourceLanguageClick: () -> Unit,
    onTargetLanguageClick: () -> Unit,
    onSwapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 300),
        label = "swap_rotation"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        // Purple accent line at the very top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(BrandPurple)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Source language button (left-aligned text)
            LanguageButton(
                language = sourceLanguage,
                onClick = onSourceLanguageClick,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )

            // Swap icon (no background circle)
            IconButton(
                onClick = {
                    rotationAngle += 180f
                    onSwapClick()
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Swap languages",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(animatedRotation)
                )
            }

            // Target language button (right-aligned text)
            LanguageButton(
                language = targetLanguage,
                onClick = onTargetLanguageClick,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }

        // Bottom divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Composable
private fun LanguageButton(
    language: Language,
    onClick: () -> Unit,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        if (textAlign == TextAlign.End) {
            Text(
                text = language.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = textAlign,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = language.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = textAlign,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
