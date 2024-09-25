// src/main/kotlin/uz/etc/components/Tooltip.kt
package uz.etc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

data class TooltipInfo(
    val text: String,
    val position: Offset
)

@Composable
fun Tooltip(text: String, position: Offset, parentSize: Size) {
    val density = LocalDensity.current
    val tooltipWidth = 150.dp
    val tooltipHeight = 50.dp
    val x = with(density) {
        min(
            position.x + 10.dp.toPx(),
            parentSize.width - tooltipWidth.toPx() - 10.dp.toPx()
        )
    }
    val y = with(density) {
        min(
            position.y + 10.dp.toPx(),
            parentSize.height - tooltipHeight.toPx() - 10.dp.toPx()
        )
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(x.toInt(), y.toInt()) }
            .background(Color.Gray.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}