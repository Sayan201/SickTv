// SPDX-License-Identifier: GPL-3.0-only

package app.sicktv.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * True OLED black canvas, translucent smoked obsidian glass containers, luminous
 * specular hairline highlights, and vivid neon crimson accents matching the Zangetsu
 * design language.
 */
object LiquidGlass {
    // Glass highlight accents - Neon Crimson (Zangetsu aesthetic)
    val NeonCrimson = Color(0xFFFF2A42)
    val NeonCrimsonSheen = Color(0x66FF2A42)
    val NeonCrimsonGlow = Color(0x33FF2A42)
    val NeonCrimsonSubtle = Color(0x18FF2A42)
    val NeonCrimsonDark = Color(0xFFE50914)
    val CrimsonGlassFill = Color(0xFF22080C)
    val CrimsonGlassFillActive = Color(0xFF380B12)

    // Backwards-compatible aliases for existing calls
    val NeonCyan = NeonCrimson
    val NeonCyanSheen = NeonCrimsonSheen
    val NeonCyanGlow = NeonCrimsonGlow

    // Glass specular lines
    val HighlightWhite = Color(0x38FFFFFF)
    val HairlineWhite = Color(0x1EFFFFFF)
    val SubtleWhite = Color(0x0CFFFFFF)

    // Smoked obsidian acrylic glass tones (neutral pitch black obsidian, zero unwanted tint)
    val SmokedAcrylicTop = Color(0xFF16161B)
    val SmokedAcrylicBottom = Color(0xFF0D0D10)
    val SmokedAcrylicHigh = Color(0xFF202026)
    val CardSurface = Color(0xFF101014)
    val OledPitchBlack = Color(0xFF000000)

    /**
     * Vertical hairline specular border: soft reflection on top, subtle transparency at bottom.
     */
    fun borderStroke(
        width: Dp = 1.dp,
        topColor: Color = Color(0x2EFFFFFF),
        bottomColor: Color = Color(0x0EFFFFFF),
    ): BorderStroke = BorderStroke(
        width = width,
        brush = Brush.verticalGradient(
            colors = listOf(topColor, bottomColor),
        ),
    )

    /**
     * Subtle crimson-tinged border for active or highlighted glass surfaces.
     */
    fun activeBorderStroke(
        width: Dp = 1.dp,
    ): BorderStroke = BorderStroke(
        width = width,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xE6FF2A42), Color(0x40FF2A42)),
        ),
    )

    /**
     * Liquid lens brush for the floating remote button: glowing deep crimson with specular highlight.
     */
    val RemoteLensBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFF334C),
            Color(0xFFE0001B),
            Color(0xFF88000E),
            Color(0xFF4A0007),
        ),
        radius = 110f,
    )

    /**
     * Dock background brush: translucent smoked obsidian acrylic glass with specular top reflection.
     */
    val DockBackgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xDD18181E),
            Color(0xF0101014),
            Color(0xF80A0A0D),
        ),
    )

    /**
     * Tile background brush for normal action tiles with translucent frosted glass.
     */
    val TileGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xCC181822),
            Color(0xE0101016),
        ),
    )

    /**
     * Active tile brush: smoked acrylic with glowing crimson undertone.
     */
    val ActiveTileGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xDD3A0D16),
            Color(0xEE1C060A),
        ),
    )

    /**
     * Card background brush: smoked frosted glass with a light specular wash on top.
     */
    val CardGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xCC16161E),
            Color(0xE60E0E13),
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
