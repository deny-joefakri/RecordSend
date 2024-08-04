package com.deny.recordsend.ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Base colors here
internal val GreenCitrus = Color(0xFF99CC00)
internal val RedPrimary = Color(0xFFD32F2F)

/**
 * Expand the final [Colors] class to provide more custom app colors.
 */
data class AppColors(
    val themeColors: Colors,

    // Custom colors here
    val topAppBarBackground: Color = RedPrimary
)

internal val LightColorPalette = AppColors(
    themeColors = lightColors()
)

internal val DarkColorPalette = AppColors(
    themeColors = darkColors()
)

internal val LocalAppColors = staticCompositionLocalOf { LightColorPalette }
