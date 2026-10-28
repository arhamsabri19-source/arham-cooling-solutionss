package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ArhamLightColorScheme = lightColorScheme(
    primary = DarkBlue800,
    onPrimary = FrostWhite,
    primaryContainer = SkyBlue100,
    onPrimaryContainer = DarkBlue900,
    secondary = SkyBlue500,
    onSecondary = FrostWhite,
    secondaryContainer = SkyBlue50,
    onSecondaryContainer = DarkBlue800,
    tertiary = SkyBlue400,
    onTertiary = DarkBlue900,
    background = SurfaceLight,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SkyBlue50,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

private val ArhamDarkColorScheme = darkColorScheme(
    primary = SkyBlue400,
    onPrimary = DarkBlue900,
    primaryContainer = DarkBlue700,
    onPrimaryContainer = SkyBlue100,
    secondary = SkyBlue300,
    onSecondary = DarkBlue900,
    secondaryContainer = DarkBlue800,
    onSecondaryContainer = SkyBlue100,
    tertiary = SkyBlue500,
    onTertiary = FrostWhite,
    background = DarkBlue900,
    onBackground = FrostWhite,
    surface = DarkBlue800,
    onSurface = FrostWhite,
    surfaceVariant = DarkBlue700,
    onSurfaceVariant = SkyBlue100,
    outline = DarkBlue600
)

@Composable
fun ArhamCoolingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // We enforce the strong brand identity by default
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> ArhamDarkColorScheme
        else -> ArhamLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
