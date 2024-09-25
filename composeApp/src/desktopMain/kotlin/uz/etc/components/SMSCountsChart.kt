package uz.etc.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.IntOffset
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Typeface
import kotlin.math.min

@Composable
fun SMSCountsChart(counts: Map<String, Int>) {
    // Prepare data
    val sortedCounts = counts.entries.sortedByDescending { it.value }
    val maxValue = sortedCounts.maxOfOrNull { it.value } ?: 0
    val primaryColor = MaterialTheme.colors.primary

    // Scroll state for horizontal scrolling
    val scrollState = rememberScrollState()

    // State to hold tooltip information
    var tooltipInfo by remember { mutableStateOf<TooltipInfo?>(null) }

    // Animated fractions for each bar
    val animatedFractions = sortedCounts.map { entry ->
        animateFloatAsState(
            targetValue = if (maxValue > 0) entry.value / maxValue.toFloat() else 0f,
            animationSpec = tween(durationMillis = 500)
        )
    }

    // Dimensions
    val barWidthDp = 40.dp
    val spacingDp = 20.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "SMS Counts Chart",
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // HorizontalScroll to contain the chart and labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .horizontalScroll(scrollState),
            verticalAlignment = Alignment.Bottom
        ) {
            // Canvas for the bars and value labels
            Canvas(
                modifier = Modifier
                    .height(300.dp) // Fixed height for the chart
                    .width(
                        // Calculate total width based on number of bars
                        (sortedCounts.size * (barWidthDp + spacingDp))
                    )
                    .padding(bottom = 60.dp) // Reserve space for service labels
                    .pointerInput(Unit) {
                        // Detect tap gestures for tooltips
                        detectTapGestures { offset ->
                            val barWidthPx = barWidthDp.toPx()
                            val spacingPx = spacingDp.toPx()
                            val barTotalWidth = barWidthPx + spacingPx
                            val barIndex = ((offset.x - spacingPx / 2) / barTotalWidth).toInt()
                            if (barIndex in sortedCounts.indices) {
                                val entry = sortedCounts[barIndex]
                                tooltipInfo = TooltipInfo(
                                    text = "${entry.key}: ${entry.value}",
                                    position = offset
                                )
                            } else {
                                tooltipInfo = null
                            }
                        }
                    }
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val barWidth = barWidthDp.toPx()
                val spacing = spacingDp.toPx()
                val maxBarHeight = canvasHeight * 0.7f // 70% of canvas height

                sortedCounts.forEachIndexed { index, entry ->
                    val animatedFraction = animatedFractions[index].value
                    val barHeight = animatedFraction * maxBarHeight

                    val x = index * (barWidth + spacing) + spacing / 2
                    val y = canvasHeight - barHeight - 30.dp.toPx() // Leave space for value labels

                    // Draw the bar with animation
                    drawRect(
                        color = primaryColor,
                        topLeft = Offset(x, y),
                        size = Size(width = barWidth, height = barHeight)
                    )

                    // Draw the value label above the bar
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            color = Color.Black.toArgb()
                        }
                        val typeface = Typeface.makeDefault()
                        val font = Font(typeface, 12.sp.toPx())
                        val valueLabel = entry.value.toString()
                        val labelX = x + barWidth / 2
                        val labelY = y - 5.dp.toPx()
                        canvas.nativeCanvas.drawString(
                            valueLabel,
                            labelX,
                            labelY,
                            font,
                            paint
                        )
                    }
                }
            }
        }

        // Row for service labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.width(spacingDp / 2))
            sortedCounts.forEachIndexed { index, entry ->
                val labelWidth = barWidthDp
                val totalWidth = barWidthDp + spacingDp
                Column(
                    modifier = Modifier
                        .width(totalWidth)
                        .padding(horizontal = spacingDp / 2),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = entry.key,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(labelWidth),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }

        // Add horizontal scrollbar
        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier
                .fillMaxWidth()
        )

        // Display tooltip if any
        tooltipInfo?.let { tooltip ->
            val density = LocalDensity.current
            val tooltipWidth = 150.dp
            val tooltipHeight = 50.dp
            val x = with(density) { tooltip.position.x.toDp() }
            val y = with(density) { tooltip.position.y.toDp() }

            Box(
                modifier = Modifier
                    .offset { IntOffset(x.roundToPx(), y.roundToPx()) }
                    .size(width = tooltipWidth, height = tooltipHeight)
                    .background(Color.Gray.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = tooltip.text,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

data class TooltipInfo(
    val text: String,
    val position: Offset
)
