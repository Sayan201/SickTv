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
    // Official Zangetsu Design System Tokens (from Spyou/Zangetsu)
    val ZangetsuBg = Color(0xFF0B0B0F)
    val ZangetsuSurface = Color(0xFF16161C)
    val ZangetsuSurface2 = Color(0xFF20212A)
    val ZangetsuTextPrimary = Color(0xFFFFFFFF)
    val ZangetsuTextSecondary = Color(0xFFA7A7B2)
    val ZangetsuTextTertiary = Color(0xFF6E6E78)
    val ZangetsuHairline = Color(0x14FFFFFF)
    val ZangetsuAccent = Color(0xFFFF4D57)
    val ZangetsuAccentSoft = Color(0x26FF4D57)

    // Glass highlight accents - Neon Crimson (Zangetsu aesthetic)
    val NeonCrimson = Color(0xFFFF4D57)
    val NeonCrimsonSheen = Color(0x66FF4D57)
    val NeonCrimsonGlow = Color(0x33FF4D57)
    val NeonCrimsonSubtle = Color(0x18FF4D57)
    val NeonCrimsonDark = Color(0xFFE0001B)
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
        topColor: Color = Color(0x75FFFFFF),
        bottomColor: Color = Color(0x12FFFFFF),
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
            colors = listOf(Color(0xE6FF4D57), Color(0x40FF4D57)),
        ),
    )

    /**
     * Crystal hairline border matching the physical glass reflections in GlassDock.
     */
    val CrystalRimBorder = BorderStroke(
        1.dp,
        Brush.verticalGradient(
            listOf(
                Color(0x80FFFFFF),
                Color(0x25FFFFFF),
                Color(0x0EFFFFFF),
            ),
        ),
    )

    /**
     * Liquid lens brush for the floating remote button: glowing deep crimson with specular highlight.
     */
    val RemoteLensBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFF4D62),
            Color(0xFFE0001B),
            Color(0xFF88000E),
            Color(0xFF420006),
        ),
        radius = 110f,
    )

    /**
     * Dock background brush: authentic Zangetsu translucent smoked obsidian acrylic glass.
     * Translucent so underlying wallpaper and scrolling items are visible through the glass capsule.
     */
    val DockBackgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x521A1A26),
            Color(0x6612121A),
            Color(0x750A0A10),
        ),
    )

    /**
     * Hairline outer border for the floating dock capsule: specular white top arc fading to subtle glass edges.
     */
    val DockHairlineBorder = BorderStroke(
        0.75.dp,
        Brush.verticalGradient(
            listOf(
                Color(0x80FFFFFF),
                Color(0x28FFFFFF),
                Color(0x10FFFFFF),
            ),
        ),
    )

    /**
     * Clear translucent 3D liquid lens body brush for the active tab indicator.
     */
    val LiquidLensBodyBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x20FFFFFF),
            Color(0x0CFFFFFF),
            Color(0x14000000),
        ),
    )

    /**
     * 3D refractive specular rim brush for the liquid lens indicator with top gleam and subtle crimson edge dispersion.
     */
    val LiquidLensRimBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xF5FFFFFF),
            Color(0x90FFFFFF),
            Color(0x35FF4D57),
            Color(0x28000000),
        ),
    )

    /**
     * Tile background brush for normal action tiles with translucent frosted crystal glass.
     */
    val TileGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x551E1E2C),
            Color(0x65141422),
            Color(0x750A0A14),
        ),
    )

    /**
     * Active tile brush: smoked crystal acrylic with glowing crimson undertone.
     */
    val ActiveTileGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x80FF4D57),
            Color(0x65E0001B),
            Color(0x7088000E),
        ),
    )

    /**
     * Card background brush: crystal translucent smoked obsidian acrylic with physical refraction depth.
     */
    val CardGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x521A1A28),
            Color(0x6012121E),
            Color(0x700A0A12),
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
