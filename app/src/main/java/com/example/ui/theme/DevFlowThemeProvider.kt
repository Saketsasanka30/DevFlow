package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Supported Theme Modes across DevFlow Developer OS.
 */
enum class ThemeMode(val label: String, val description: String) {
    SYSTEM("System Default", "Follows Android system daylight / dark appearance"),
    LIGHT("Light Mode", "High-contrast clean studio layout for daylight coding"),
    DARK("Dark Mode", "Developer obsidian & slate palette with glowing accents")
}

/**
 * Controller exposed to any Composable via LocalThemeController.current
 */
data class ThemeController(
    val mode: ThemeMode,
    val isDark: Boolean,
    val toggleTheme: () -> Unit,
    val setMode: (ThemeMode) -> Unit
)

val LocalThemeController = staticCompositionLocalOf<ThemeController> {
    ThemeController(
        mode = ThemeMode.DARK,
        isDark = true,
        toggleTheme = {},
        setMode = {}
    )
}

/**
 * Application-wide Theme Provider Composable that enforces dynamic ThemeMode
 * (System, Light, Dark) across the entire application interface.
 */
@Composable
fun DevFlowThemeProvider(
    themeMode: ThemeMode,
    onSetThemeMode: (ThemeMode) -> Unit,
    onToggleTheme: () -> Unit,
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val effectiveIsDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemInDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val controller = ThemeController(
        mode = themeMode,
        isDark = effectiveIsDark,
        toggleTheme = onToggleTheme,
        setMode = onSetThemeMode
    )

    MyApplicationTheme(darkTheme = effectiveIsDark) {
        CompositionLocalProvider(LocalThemeController provides controller) {
            content()
        }
    }
}
