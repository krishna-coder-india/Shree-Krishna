package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = SaffronLight,
    onPrimary = Color(0xFF4A1000),
    primaryContainer = SaffronDark,
    onPrimaryContainer = SaffronContainer,
    secondary = GoldAccent,
    onSecondary = Color(0xFF332000),
    secondaryContainer = Color(0xFF523500),
    onSecondaryContainer = GoldContainer,
    tertiary = PeacockTealLight,
    onTertiary = Color(0xFF003730),
    background = DarkBg,
    onBackground = DarkOnBg,
    surface = DarkSurface,
    onSurface = DarkOnBg,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextMuted,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = SaffronContainer,
    onPrimaryContainer = OnSaffronContainer,
    secondary = GoldSecondary,
    onSecondary = Color.White,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = OnGoldContainer,
    tertiary = PeacockTeal,
    onTertiary = Color.White,
    tertiaryContainer = PeacockTealContainer,
    onTertiaryContainer = Color(0xFF00201B),
    background = ParchmentBgLight,
    onBackground = OnBgLight,
    surface = SurfaceLight,
    onSurface = OnBgLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextMutedLight,
  )

@Composable
fun BhagwatTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For devotional themes, keeping brand colors creates the authentic temple atmosphere
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

// Retain alias for backward compatibility
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = BhagwatTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
