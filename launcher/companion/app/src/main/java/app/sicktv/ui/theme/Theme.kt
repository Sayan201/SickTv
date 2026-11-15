// SPDX-License-Identifier: GPL-3.0-only

package app.sicktv.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Branded fallback for phones without dynamic color (Android 11 and older).
// Seed: a deep teal, picked to read as "screen glow" without clashing with app icon tiles.
// Every role is set, tonal-spot tones from that seed: a role left out falls back to
// Material's stock purple.
private val LightBrand = lightColorScheme(
    primary = Color(0xFF00696B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF9CF1F2),
    onPrimaryContainer = Color(0xFF002020),
    inversePrimary = Color(0xFF80D4D5),
    secondary = Color(0xFF4A6363),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCE8E8),
    onSecondaryContainer = Color(0xFF051F1F),
    tertiary = Color(0xFF4B607C),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD3E4FF),
    onTertiaryContainer = Color(0xFF041C35),
    background = Color(0xFFF4FBFA),
    onBackground = Color(0xFF161D1D),
    surface = Color(0xFFF4FBFA),
    onSurface = Color(0xFF161D1D),
    surfaceVariant = Color(0xFFDAE4E4),
    onSurfaceVariant = Color(0xFF3F4948),
    inverseSurface = Color(0xFF2B3232),
    inverseOnSurface = Color(0xFFECF2F1),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF6F7979),
    outlineVariant = Color(0xFFBEC8C8),
    surfaceBright = Color(0xFFF4FBFA),
    surfaceContainer = Color(0xFFE9EFEE),
    surfaceContainerHigh = Color(0xFFE3E9E9),
    surfaceContainerHighest = Color(0xFFDDE4E3),
    surfaceContainerLow = Color(0xFFEEF5F4),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFD5DBDB),
    primaryFixed = Color(0xFF9CF1F2),
    primaryFixedDim = Color(0xFF80D4D5),
    onPrimaryFixed = Color(0xFF002020),
    onPrimaryFixedVariant = Color(0xFF004F50),
    secondaryFixed = Color(0xFFCCE8E7),
    secondaryFixedDim = Color(0xFFB0CCCB),
    onSecondaryFixed = Color(0xFF051F1F),
    onSecondaryFixedVariant = Color(0xFF324B4B),
    tertiaryFixed = Color(0xFFD3E4FF),
    tertiaryFixedDim = Color(0xFFB3C8E8),
    onTertiaryFixed = Color(0xFF041C35),
    onTertiaryFixedVariant = Color(0xFF334863),
)

private val DarkBrand = darkColorScheme(
    primary = Color(0xFF00E5FF),
    onPrimary = Color(0xFF00232B),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF80F2FF),
    inversePrimary = Color(0xFF006874),
    secondary = Color(0xFF90CEE2),
    onSecondary = Color(0xFF00343F),
    secondaryContainer = Color(0xFF141418),
    onSecondaryContainer = Color(0xFFE2E8F0),
    tertiary = Color(0xFF00E5FF),
    onTertiary = Color(0xFF00232B),
    tertiaryContainer = Color(0xFF141418),
    onTertiaryContainer = Color(0xFFE2E8F0),
    background = Color(0xFF000000),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF141416),
    onSurfaceVariant = Color(0xFFA0A0AB),
    inverseSurface = Color(0xFFE2E8F0),
    inverseOnSurface = Color(0xFF0B0B0D),
    error = Color(0xFFFF6B6B),
    onError = Color(0xFF490008),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0x28FFFFFF),
    outlineVariant = Color(0x12FFFFFF),
    surfaceBright = Color(0xFF242428),
    surfaceContainer = Color(0xFF121215),
    surfaceContainerHigh = Color(0xFF18181C),
    surfaceContainerHighest = Color(0xFF222226),
    surfaceContainerLow = Color(0xFF0D0D10),
    surfaceContainerLowest = Color(0xFF000000),
    surfaceDim = Color(0xFF000000),
    primaryFixed = Color(0xFF80F2FF),
    primaryFixedDim = Color(0xFF00E5FF),
    onPrimaryFixed = Color(0xFF002025),
    onPrimaryFixedVariant = Color(0xFF004F58),
    secondaryFixed = Color(0xFFC7EBF6),
    secondaryFixedDim = Color(0xFF90CEE2),
    onSecondaryFixed = Color(0xFF001F26),
    onSecondaryFixedVariant = Color(0xFF1A3844),
    tertiaryFixed = Color(0xFF80F2FF),
    tertiaryFixedDim = Color(0xFF00E5FF),
    onTertiaryFixed = Color(0xFF002025),
    onTertiaryFixedVariant = Color(0xFF004F58),
)

/** Numbers come first: the app font with tabular figures, so values line up and don't jitter as they update. */
val StatNumber = TextStyle(fontWeight = FontWeight.Medium, fontSize = 28.sp, lineHeight = 34.sp, fontFeatureSettings = "tnum")
val SmallNumber = TextStyle(fontSize = 13.sp, lineHeight = 18.sp, fontFeatureSettings = "tnum")

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SickTvTheme(
    dark: Boolean = isSystemInDarkTheme(),
    dynamic: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colors = when {
        dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        dark -> DarkBrand
        else -> LightBrand
    }
    MaterialExpressiveTheme(
        colorScheme = colors,
        motionScheme = MotionScheme.expressive(),
        typography = Typography(),
        content = content,
    )
}
