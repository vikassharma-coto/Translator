package com.usbrous.trans.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary             = Purple80,
    onPrimary           = Purple20,
    primaryContainer    = Purple30,
    onPrimaryContainer  = Purple90,
    secondary           = Magenta80,
    onSecondary         = Color(0xFF4A0057),
    secondaryContainer  = Color(0xFF660075),
    onSecondaryContainer = Magenta90,
    background          = SurfaceDark,
    onBackground        = Neutral90,
    surface             = CardDark,
    onSurface           = Neutral90,
    surfaceVariant      = Color(0xFF2A2A3D),
    onSurfaceVariant    = Neutral90,
    error               = Red80,
    onError             = Red40,
    errorContainer      = Color(0xFF93000A),
    onErrorContainer    = Red90,
)

private val LightColorScheme = lightColorScheme(
    primary             = Purple40,
    onPrimary           = Neutral99,
    primaryContainer    = Purple90,
    onPrimaryContainer  = Purple10,
    secondary           = Magenta40,
    onSecondary         = Neutral99,
    secondaryContainer  = Magenta90,
    onSecondaryContainer = Color(0xFF2D0036),
    background          = SurfaceLight,
    onBackground        = Neutral10,
    surface             = CardLight,
    onSurface           = Neutral10,
    surfaceVariant      = Color(0xFFF0F0F8),
    onSurfaceVariant    = Neutral20,
    error               = Red40,
    onError             = Neutral99,
    errorContainer      = Red90,
    onErrorContainer    = Color(0xFF410002),
)

@Composable
fun TransTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
