// SPDX-License-Identifier: GPL-3.0-only

package app.sicktv.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.math.abs
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pure Liquid Glass UI primitives built directly on Compose Foundation.
 * Implements the authentic Zangetsu OLED black & vivid neon crimson design system.
 */

/**
 * Modern pill button: crisp white pill for primary actions, smoked obsidian capsule for secondary/tonal actions.
 */
@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    primary: Boolean = true,
    shape: Shape = CircleShape,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
    content: @Composable RowScope.() -> Unit,
) {
    val backgroundBrush = when {
        !enabled -> SolidColor(Color(0x0CFFFFFF))
        primary -> SolidColor(Color.White)
        else -> SolidColor(Color(0xFF16161B))
    }

    val contentColor = when {
        !enabled -> Color(0x60FFFFFF)
        primary -> Color(0xFF000000)
        else -> Color.White
    }

    val borderStroke = when {
        !enabled -> null
        primary -> null
        else -> LiquidGlass.borderStroke(1.dp)
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundBrush, shape)
            .then(if (borderStroke != null) Modifier.border(borderStroke, shape) else Modifier)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    content = content,
                )
            }
        }
    }
}

/**
 * Crisp white Floating Action Button matching the design reference (+ Install app, Upload files).
 */
@Composable
fun GlassFab(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .shadow(elevation = 12.dp, shape = CircleShape, spotColor = Color(0x80000000))
            .clip(CircleShape)
            .background(Color.White, CircleShape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Black,
            )
            Text(
                text = text,
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                ),
            )
        }
    }
}

/**
 * Segmented filter capsule bar matching the design reference (Running | Installed | Built-in).
 */
@Composable
fun GlassSegmentedBar(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF121216), CircleShape)
            .border(BorderStroke(1.dp, Color(0x24FFFFFF)), CircleShape),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEachIndexed { index, title ->
                val selected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .then(
                            if (selected) {
                                Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF380B12), CircleShape)
                                    .border(BorderStroke(1.dp, Color(0x80FF2A42)), CircleShape)
                            } else Modifier
                        )
                        .clickable(enabled = enabled) { onSelect(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        if (selected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = LiquidGlass.NeonCrimson,
                            )
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 13.sp,
                            ),
                            color = if (selected) Color.White else Color(0x8AFFFFFF),
                            maxLines = 1,
                        )
                    }
                }
                if (index < options.size - 1 && !selected && index + 1 != selectedIndex) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0x18FFFFFF)),
                    )
                }
            }
        }
    }
}

/**
 * Capsule Search Bar matching the design reference.
 */
@Composable
fun GlassSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search apps",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(enabled = enabled, onClick = onClick)
    } else Modifier

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(CircleShape)
            .background(Color(0xFF141418), CircleShape)
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), CircleShape)
            .then(clickableModifier)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0x80FFFFFF),
            )
            Spacer(Modifier.width(12.dp))
            if (onClick != null) {
                Text(
                    text = query.ifEmpty { placeholder },
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = if (query.isEmpty()) Color(0x60FFFFFF) else Color.White,
                    modifier = Modifier.weight(1f),
                )
            } else {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            color = Color(0x60FFFFFF),
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        enabled = enabled,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        cursorBrush = SolidColor(LiquidGlass.NeonCrimson),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                if (query.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { onQueryChange("") },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0x80FFFFFF),
                        )
                    }
                }
            }
        }
    }
}

/**
 * Pure Foundation animated toggle switch matching the design reference.
 */
@Composable
fun GlassSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val trackWidth = 50.dp
    val trackHeight = 28.dp
    val thumbSize = 22.dp
    val thumbPadding = 3.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - thumbPadding else thumbPadding,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "switchThumb",
    )

    val trackColor = when {
        !enabled -> Color(0xFF1E1E24)
        checked -> Color(0xFFD61E34)
        else -> Color(0xFF26262E)
    }

    val trackBorder = when {
        !enabled -> BorderStroke(1.dp, Color(0x10FFFFFF))
        checked -> BorderStroke(1.dp, Color(0xB3FF2A42))
        else -> BorderStroke(1.dp, Color(0x26FFFFFF))
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(width = trackWidth, height = trackHeight)
            .clip(CircleShape)
            .background(trackColor, CircleShape)
            .border(trackBorder, CircleShape)
            .then(
                if (onCheckedChange != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        onClick = { onCheckedChange(!checked) },
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .background(Color.White, CircleShape),
        )
    }
}

/**
 * Pure Foundation Linear Progress Indicator with vivid crimson gradient.
 */
@Composable
fun GlassLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Color(0xFF1C1C22),
    progressBrush: Brush = Brush.horizontalGradient(
        listOf(Color(0xFFFF384E), Color(0xFFD61E34)),
    ),
) {
    val clamped = progress.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .height(8.dp)
            .clip(CircleShape)
            .background(trackColor, CircleShape),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(clamped)
                .clip(CircleShape)
                .background(progressBrush, CircleShape),
        )
    }
}

/**
 * Glowing pulsing crimson orb indicator for loading states (Apps & Files).
 */
@Composable
fun PulsingOrb(
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsingOrb")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "orbScale",
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.80f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "orbGlow",
    )

    Box(
        modifier = modifier.size(size * 1.6f),
        contentAlignment = Alignment.Center,
    ) {
        // Outer radiant glow halo
        Box(
            modifier = Modifier
                .size(size * 1.5f * scale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF1E36).copy(alpha = 0.35f * glowAlpha),
                            Color(0xFFFF1E36).copy(alpha = 0.08f * glowAlpha),
                            Color.Transparent,
                        ),
                    ),
                ),
        )
        // Mid glowing sphere
        Box(
            modifier = Modifier
                .size(size * scale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF334B),
                            Color(0xFFE50914),
                            Color(0xFF6B000C),
                        ),
                    ),
                ),
        )
        // Core highlight
        Box(
            modifier = Modifier
                .size(size * 0.45f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF808F),
                            Color.Transparent,
                        ),
                    ),
                )
                .alpha(glowAlpha),
        )
    }
}

/**
 * Translucent glass chip for filter and selection lists.
 */
@Composable
fun GlassChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = CircleShape,
) {
    val backgroundBrush = when {
        !enabled -> SolidColor(Color(0x06FFFFFF))
        selected -> SolidColor(Color(0xFF380B12))
        else -> SolidColor(Color(0xFF141418))
    }

    val borderStroke = when {
        !enabled -> null
        selected -> LiquidGlass.activeBorderStroke(1.dp)
        else -> LiquidGlass.borderStroke(1.dp)
    }

    val contentColor = when {
        !enabled -> Color(0x50FFFFFF)
        selected -> LiquidGlass.NeonCrimson
        else -> Color.White
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundBrush, shape)
            .then(if (borderStroke != null) Modifier.border(borderStroke, shape) else Modifier)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(value = MaterialTheme.typography.labelMedium.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                    }
                    label()
                }
            }
        }
    }
}

/**
 * Pure Liquid Glass Card container.
 * Smoked obsidian gradient fill with specular hairline border, built directly on Compose Foundation.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    border: BorderStroke? = LiquidGlass.borderStroke(1.dp),
    backgroundBrush: Brush = LiquidGlass.CardGlassBrush,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val clickableModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundBrush, shape)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .then(clickableModifier)
            .padding(contentPadding),
    ) {
        Column(content = content)
    }
}

/**
 * Translucent smoked obsidian banner for offline/warning/info alerts.
 */
@Composable
fun GlassBanner(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    message: String? = null,
    action: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(20.dp),
    border: BorderStroke = LiquidGlass.borderStroke(1.dp),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(LiquidGlass.CardGlassBrush, shape)
            .border(border, shape)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (icon != null) {
                        icon()
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                    )
                }
                if (message != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xCCFFFFFF),
                    )
                }
            }
            if (action != null) {
                Spacer(Modifier.width(12.dp))
                action()
            }
        }
    }
}

/**
 * Custom Liquid Glass floating bottom dock container with animated sliding indicator.
 * Implements the authentic Zangetsu floating capsule design system with fluid liquid droplet morphing.
 */
@Composable
fun GlassDock(
    shape: Shape = CircleShape,
    modifier: Modifier = Modifier,
    indicatorIndex: Float? = null,
    targetIndex: Float? = null,
    itemCount: Int = 4,
    content: @Composable RowScope.() -> Unit,
) {
    var widthPx by remember { mutableIntStateOf(0) }
    Box(
        modifier = modifier
            .height(60.dp)
            .onSizeChanged { widthPx = it.width }
            .shadow(
                elevation = 16.dp,
                shape = shape,
                spotColor = Color(0x66000000),
                ambientColor = Color(0x33000000),
            )
            .clip(shape)
            .background(LiquidGlass.DockBackgroundBrush, shape)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(Color(0x38FFFFFF), Color(0x0CFFFFFF)),
                    ),
                ),
                shape,
            ),
    ) {
        if (indicatorIndex != null && widthPx > 0 && itemCount > 0) {
            val slotWidth = widthPx.toFloat() / itemCount
            val diff = if (targetIndex != null) abs(targetIndex - indicatorIndex) else 0f
            val stretchFactor = (diff * 0.28f).coerceIn(0f, 0.38f)
            val extraWidth = slotWidth * stretchFactor
            val movingRight = targetIndex != null && targetIndex >= indicatorIndex
            val dynamicOffset = if (stretchFactor > 0.01f) {
                if (movingRight) (indicatorIndex * slotWidth) - (extraWidth * 0.15f)
                else (indicatorIndex * slotWidth) - (extraWidth * 0.85f)
            } else {
                indicatorIndex * slotWidth
            }
            val indicatorWidth = slotWidth + extraWidth
            val scaleY = 1f - (stretchFactor * 0.15f)

            Box(
                modifier = Modifier
                    .offset { IntOffset(dynamicOffset.toInt(), 0) }
                    .width(with(LocalDensity.current) { indicatorWidth.toDp() })
                    .fillMaxHeight()
                    .graphicsLayer { this.scaleY = scaleY }
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .shadow(
                        elevation = (12 + (stretchFactor * 10)).dp,
                        shape = CircleShape,
                        spotColor = Color(0xFFFF2A42),
                        ambientColor = Color(0xFFFF2A42),
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color(0xFFFF263E),
                                Color(0xFFD60018),
                                Color(0xFF8A000F),
                            ),
                        ),
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            Brush.verticalGradient(
                                listOf(Color(0x99FFFFFF), Color(0x40FF2A42)),
                            ),
                        ),
                        CircleShape,
                    ),
            ) {
                // Specular gloss crescent reflection for true liquid glass surface
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.48f)
                        .align(Alignment.TopCenter)
                        .clip(RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0x55FFFFFF), Color.Transparent),
                            ),
                        ),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            content = content,
        )
    }
}

/**
 * Single navigation item inside the [GlassDock] with fluid indicator animation.
 * Follows the Zangetsu styling: pure white icon and text when selected over the red pill,
 * and muted smoked white when unselected, with an animated liquid scale pop.
 */
@Composable
fun RowScope.GlassDockItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val iconScale by animateFloatAsState(
        targetValue = if (selected) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "dockItemScale",
    )
    val contentColor = if (selected) Color.White else Color(0x99FFFFFF)
    val labelColor = if (selected) Color.White else Color(0x80FFFFFF)

    Column(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick,
            )
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.scale(iconScale),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                icon()
            }
        }
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp,
            ),
            color = labelColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
