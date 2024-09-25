// src/main/kotlin/uz/etc/components/BarChart.kt
package uz.etc.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Typeface
import kotlin.math.min

@Composable
fun BarChart(
    data: List<Map.Entry<String, Int>>,
    modifier: Modifier = Modifier,
    tooltipInfo: TooltipInfo?,
    setTooltipInfo: (TooltipInfo?) -> Unit
) {
    val sortedData = data.sortedByDescending { it.value }
    val maxValue = sortedData.maxOfOrNull { it.value } ?: 0
    val primaryColor = MaterialTheme.colors.primary

    val barWidthDp = 40.dp
    val spacingDp = 20.dp

    val animatedFractions = sortedData.map { entry ->
        animateFloatAsState(
            targetValue = if (maxValue > 0) entry.value / maxValue.toFloat() else 0f,
            animationSpec = tween(durationMillis = 500)
        )
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = spacingDp / 2),
        horizontalArrangement = Arrangement.spacedBy(spacingDp)
    ) {
        itemsIndexed(sortedData) { index, entry ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(barWidthDp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            setTooltipInfo(
                                TooltipInfo(
                                    text = "${entry.key}: ${entry.value}",
                                    position = offset
                                )
                            )
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .height(200.dp) // Fixed height for the bar chart
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val barHeight = size.height * animatedFractions[index].value
                        drawRect(
                            color = primaryColor,
                            topLeft = Offset(0f, size.height - barHeight),
                            size = Size(width = size.width, height = barHeight)
                        )

                        // Draw the value label above the bar
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                color = Color.Black.toArgb()
                            }
                            val typeface = Typeface.makeDefault()
                            val fontSizePx = 12.sp.toPx()
                            val font = org.jetbrains.skia.Font(typeface, fontSizePx)
                            val valueLabel = entry.value.toString()
                            val labelX = size.width / 2
                            val labelY = size.height - barHeight - 5.dp.toPx()
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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = entry.key,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(barWidthDp)
                )
            }
        }
    }
}
