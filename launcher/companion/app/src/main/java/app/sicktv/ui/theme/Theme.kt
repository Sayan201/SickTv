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
private val LightBrand = lightColorScheme(
    primary = Color(0xFFC00024),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDADE),
    onPrimaryContainer = Color(0xFF400008),
    inversePrimary = Color(0xFFFFB3BA),
    secondary = Color(0xFF775658),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFD9DC),
    onSecondaryContainer = Color(0xFF2C1517),
    tertiary = Color(0xFF755A2F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDDB1),
    onTertiaryContainer = Color(0xFF291800),
    background = Color(0xFFFCF8F8),
    onBackground = Color(0xFF211A1A),
    surface = Color(0xFFFCF8F8),
    onSurface = Color(0xFF211A1A),
    surfaceVariant = Color(0xFFF4DDDE),
    onSurfaceVariant = Color(0xFF534344),
    inverseSurface = Color(0xFF372E2F),
    inverseOnSurface = Color(0xFFFDEDEE),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF857374),
    outlineVariant = Color(0xFFD8C2C3),
)

// Zangetsu OLED dark theme: Pure OLED Pitch Black canvas, Smoked Obsidian containers, Vivid Neon Crimson accents
private val DarkBrand = darkColorScheme(
    primary = Color(0xFFFF4D57),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF22080C),
    onPrimaryContainer = Color(0xFFFFDADE),
    inversePrimary = Color(0xFFFFB3BA),
    secondary = Color(0xFFE2B6BA),
    onSecondary = Color(0xFF44191E),
    secondaryContainer = Color(0xFF16161C),
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = Color(0xFFFF4D57),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF22080C),
    onTertiaryContainer = Color(0xFFFFDADE),
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF0B0B0F),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF16161C),
    onSurfaceVariant = Color(0xFFA7A7B2),
    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF0B0B0D),
    error = Color(0xFFFF495F),
    onError = Color(0xFF490008),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0x28FFFFFF),
    outlineVariant = Color(0x14FFFFFF),
    surfaceBright = Color(0xFF202026),
    surfaceContainer = Color(0xFF16161C),
    surfaceContainerHigh = Color(0xFF20212A),
    surfaceContainerHighest = Color(0xFF2A2B36),
    surfaceContainerLow = Color(0xFF0B0B0F),
    surfaceContainerLowest = Color(0xFF000000),
    surfaceDim = Color(0xFF000000),
    primaryFixed = Color(0xFFFFDADE),
    primaryFixedDim = Color(0xFFFFB3BA),
    onPrimaryFixed = Color(0xFF400008),
    onPrimaryFixedVariant = Color(0xFF730015),
    secondaryFixed = Color(0xFFFFD9DC),
    secondaryFixedDim = Color(0xFFE2B6BA),
    onSecondaryFixed = Color(0xFF2C1517),
    onSecondaryFixedVariant = Color(0xFF5D3F41),
    tertiaryFixed = Color(0xFFFFDADE),
    tertiaryFixedDim = Color(0xFFFF4D57),
    onTertiaryFixed = Color(0xFF400008),
    onTertiaryFixedVariant = Color(0xFF730015),
)

/** Numbers come first: the app font with tabular figures, so values line up and don't jitter as they update. */
val StatNumber = TextStyle(fontWeight = FontWeight.Medium, fontSize = 28.sp, lineHeight = 34.sp, fontFeatureSettings = "tnum")
val SmallNumber = TextStyle(fontSize = 13.sp, lineHeight = 18.sp, fontFeatureSettings = "tnum")

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SickTvTheme(
    dark: Boolean = true,
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
    ) {
        androidx.compose.runtime.CompositionLocalProvider(
            androidx.compose.material3.LocalContentColor provides (if (dark) Color.White else Color(0xFF211A1A)),
            content = content,
        )
    }
}
