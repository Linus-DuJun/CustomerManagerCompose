package org.linus.core.ui.common

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

@Composable
fun AutoSizedCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary
) {
    BoxWithConstraints(modifier) {
        val diameter = with(LocalDensity.current) {
            min(constraints.maxWidth.toDp(), constraints.maxHeight.toDp()) - InternalPadding
        }
        CircularProgressIndicator(
            strokeWidth = (diameter * StrokeDiameterFraction).coerceAtLeast(1.dp),
            color = color
        )
    }
}

// Default stroke size
private val DefaultStrokeWidth = 4.dp

// Preferred diameter for CircularProgressIndicator
private val DefaultDiameter = 40.dp

// Internal padding added by CircularProgressIndicator
private val InternalPadding = 4.dp

private val StrokeDiameterFraction = DefaultStrokeWidth / DefaultDiameter