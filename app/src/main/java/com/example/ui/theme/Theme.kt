package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DevPrimaryCyan,
    onPrimary = DevBgDark,
    primaryContainer = DevSurfaceElevated,
    onPrimaryContainer = DevPrimaryCyan,
    secondary = DevAccentPurple,
    onSecondary = DevBgDark,
    secondaryContainer = DevSurfaceCardDark,
    onSecondaryContainer = DevAccentPurple,
    tertiary = DevAccentGreen,
    onTertiary = DevBgDark,
    background = DevBgDark,
    onBackground = DevTextPrimary,
    surface = DevSurfaceDark,
    onSurface = DevTextPrimary,
    surfaceVariant = DevSurfaceCardDark,
    onSurfaceVariant = DevTextSecondary,
    outline = DevBorderDark,
    outlineVariant = DevBorderSubtle,
    error = DevAccentRed,
    onError = DevBgDark
)

private val LightColorScheme = lightColorScheme(
    primary = DevPrimaryGlow,
    onPrimary = DevBgLight,
    primaryContainer = DevSurfaceCardLight,
    onPrimaryContainer = DevPrimaryGlow,
    secondary = DevAccentPurple,
    onSecondary = DevBgLight,
    secondaryContainer = DevSurfaceCardLight,
    onSecondaryContainer = DevAccentPurple,
    tertiary = DevAccentGreen,
    onTertiary = DevBgLight,
    background = DevBgLight,
    onBackground = DevTextPrimaryLight,
    surface = DevSurfaceLight,
    onSurface = DevTextPrimaryLight,
    surfaceVariant = DevSurfaceCardLight,
    onSurfaceVariant = DevTextSecondaryLight,
    outline = DevBorderLight,
    outlineVariant = DevBorderLight,
    error = DevAccentRed,
    onError = DevBgLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek developer dark mode
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

