// SPDX-License-Identifier: GPL-3.0-only

package app.sicktv.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Liquid Glass styling utilities for the SickTv Companion design system.
 * Replaces flat surfaces with translucent smoked glass containers, luminous
 * specular hairline highlights, and electric cyan neon accents to match
 * the SickTv Launcher visual language.
 */
object LiquidGlass {
    // Glass highlight accents
    val HighlightWhite = Color(0x38FFFFFF)
    val HairlineWhite = Color(0x1AFFFFFF)
    val SubtleWhite = Color(0x0CFFFFFF)
    val NeonCyan = Color(0xFF00E5FF)
    val NeonCyanSheen = Color(0x6600E5FF)
    val NeonCyanGlow = Color(0x2200E5FF)

    // Smoked acrylic glass tones
    val SmokedAcrylicTop = Color(0xFF161E2C)
    val SmokedAcrylicBottom = Color(0xFF0E1420)
    val SmokedAcrylicHigh = Color(0xFF1E283A)

    /**
     * Vertical hairline specular border: bright reflection on top, subtle transparency at bottom.
     */
    fun borderStroke(
        width: Dp = 1.dp,
        topColor: Color = Color(0x38FFFFFF),
        bottomColor: Color = Color(0x0EFFFFFF),
    ): BorderStroke = BorderStroke(
        width = width,
        brush = Brush.verticalGradient(
            colors = listOf(topColor, bottomColor),
        ),
    )

    /**
     * Subtle cyan-tinged border for active or highlighted glass surfaces.
     */
    fun activeBorderStroke(
        width: Dp = 1.dp,
    ): BorderStroke = BorderStroke(
        width = width,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0x8000E5FF), Color(0x2000E5FF)),
        ),
    )

    /**
     * Translucent smoked acrylic card colors.
     */
    @Composable
    fun cardColors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
    ): CardColors = CardDefaults.cardColors(
        containerColor = containerColor,
        contentColor = contentColor,
    )

    /**
     * Liquid lens brush for the floating remote button: glowing electric cyan with top specular highlight.
     */
    val RemoteLensBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF33ECFF),
            Color(0xFF00E5FF),
            Color(0xFF009AB0),
        ),
    )

    /**
     * Dock background brush: subtle specular sheen at the upper edge tapering into obsidian acrylic.
     */
    val DockBackgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A2332),
            Color(0xFF121924),
            Color(0xFF0B1018),
        ),
    )

    /**
     * Card background brush: smoked frosted glass with a light specular wash on top.
     */
    val CardGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x332A3950),
            Color(0x1A141D2B),
        ),
    )
}

/**
 * Applies a liquid glass frosted finish with a specular hairline border.
 */
fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(16.dp),
    borderWidth: Dp = 1.dp,
    borderColorTop: Color = Color(0x38FFFFFF),
    borderColorBottom: Color = Color(0x0EFFFFFF),
    backgroundBrush: Brush = LiquidGlass.CardGlassBrush,
): Modifier = this
    .clip(shape)
    .background(backgroundBrush, shape)
    .border(
        BorderStroke(
            borderWidth,
            Brush.verticalGradient(listOf(borderColorTop, borderColorBottom)),
        ),
        shape,
    )
