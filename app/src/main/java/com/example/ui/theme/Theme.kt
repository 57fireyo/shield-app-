package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Clean, luminous White + Greenery + Red + Black Light Scheme
private val CleanWhiteGreeneryScheme = lightColorScheme(
    primary = GreeneryPrimary,
    onPrimary = PureWhite,
    primaryContainer = GreeneryContainer,
    onPrimaryContainer = GreeneryContainerText,
    secondary = ObsidianBlack,
    onSecondary = PureWhite,
    secondaryContainer = SurfaceSubtle,
    onSecondaryContainer = ObsidianBlack,
    tertiary = RedAlertPrimary,
    onTertiary = PureWhite,
    tertiaryContainer = RedAlertContainer,
    onTertiaryContainer = RedAlertContainerText,
    background = PureWhite,
    onBackground = ObsidianBlack,
    surface = PureWhite,
    onSurface = ObsidianBlack,
    surfaceVariant = SurfaceSubtle,
    onSurfaceVariant = CharcoalMuted,
    outline = BorderSubtle,
    error = RedAlertPrimary,
    onError = PureWhite,
    errorContainer = RedAlertContainer,
    onErrorContainer = RedAlertContainerText
)

// High-contrast Obsidian & Greenery Dark Scheme
private val ObsidianGreeneryDarkScheme = darkColorScheme(
    primary = GreeneryLight,
    onPrimary = PureBlack,
    primaryContainer = GreeneryDark,
    onPrimaryContainer = GreeneryContainer,
    secondary = PureWhite,
    onSecondary = PureBlack,
    secondaryContainer = CharcoalDark,
    onSecondaryContainer = PureWhite,
    tertiary = RedAlertBright,
    onTertiary = PureWhite,
    tertiaryContainer = RedAlertDark,
    onTertiaryContainer = RedAlertContainer,
    background = ObsidianBlack,
    onBackground = PureWhite,
    surface = CharcoalDark,
    onSurface = PureWhite,
    surfaceVariant = CharcoalDark,
    onSurfaceVariant = BorderMedium,
    outline = CharcoalText,
    error = RedAlertBright,
    onError = PureBlack,
    errorContainer = RedAlertDark,
    onErrorContainer = PureWhite
)

@Composable
fun SlopeWatchTheme(
    darkTheme: Boolean = false, // Clean, crisp white & greenery by default
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> ObsidianGreeneryDarkScheme
        else -> CleanWhiteGreeneryScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
