package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val VeloraLightColorScheme = lightColorScheme(
    primary = VeloraGold,
    onPrimary = VeloraWhite,
    primaryContainer = VeloraGoldLight,
    onPrimaryContainer = VeloraInk,
    secondary = VeloraGoldDark,
    onSecondary = VeloraWhite,
    secondaryContainer = VeloraGoldSoft,
    onSecondaryContainer = VeloraInk,
    tertiary = VeloraInk,
    onTertiary = VeloraGoldLight,
    background = VeloraCream,
    onBackground = VeloraInk,
    surface = VeloraWhite,
    onSurface = VeloraInk,
    surfaceVariant = VeloraWarmSurface,
    onSurfaceVariant = VeloraMuted,
    outline = VeloraBorderGold,
    outlineVariant = VeloraGoldLight,
    error = VeloraError,
    onError = VeloraWhite
)

private val VeloraDarkColorScheme = darkColorScheme(
    primary = VeloraGold,
    onPrimary = VeloraInk,
    primaryContainer = VeloraGoldDark,
    onPrimaryContainer = VeloraGoldLight,
    secondary = VeloraGoldLight,
    onSecondary = VeloraInk,
    secondaryContainer = VeloraObsidianCard,
    onSecondaryContainer = VeloraGoldLight,
    tertiary = VeloraGoldSoft,
    onTertiary = VeloraInk,
    background = VeloraInk,
    onBackground = VeloraCream,
    surface = VeloraObsidianCard,
    onSurface = VeloraCream,
    surfaceVariant = VeloraObsidianCard,
    onSurfaceVariant = VeloraMutedLight,
    outline = VeloraBorderGold,
    outlineVariant = VeloraGoldDark,
    error = VeloraError,
    onError = VeloraWhite
)

val VeloraShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun VeloraHomeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) VeloraDarkColorScheme else VeloraLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VeloraTypography,
        shapes = VeloraShapes,
        content = content
    )
}
