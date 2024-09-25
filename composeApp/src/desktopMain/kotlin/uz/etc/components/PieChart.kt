package uz.etc.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.sqrt

@Composable
fun PieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    tooltipInfo: TooltipInfo?,
    setTooltipInfo: (TooltipInfo?) -> Unit
) {
    val total = data.sumOf { it.count }.toFloat()
    var animatedSweepAngles by remember { mutableStateOf<List<Float>>(List(data.size) { 0f }) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(total, data) {
        animatedSweepAngles = data.map { 0f } // Reset
        data.forEachIndexed { index, _ ->
            kotlinx.coroutines.delay(100) // Delay between each slice animation
            val sweepAngle = (data[index].count / total) * 360f
            animatedSweepAngles = animatedSweepAngles.toMutableList().also {
                it[index] = sweepAngle
            }
        }
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(data) {
                    detectTapGestures { offset ->
                        val centerX = canvasSize.width / 2
                        val centerY = canvasSize.height / 2
                        val dx = offset.x - centerX
                        val dy = offset.y - centerY
                        val distance = sqrt(dx * dx + dy * dy)
                        val radius = min(canvasSize.width, canvasSize.height) / 2
                        if (distance <= radius) {
                            var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat() + 90f
                            if (angle < 0f) angle += 360f
                            var startAngle = 0f
                            data.forEachIndexed { index, pieData ->
                                val sweepAngle = animatedSweepAngles.getOrNull(index) ?: 0f
                                if (angle in startAngle..(startAngle + sweepAngle)) {
                                    setTooltipInfo(
                                        TooltipInfo(
                                            text = "${pieData.serviceName}: ${pieData.count}",
                                            position = Offset(x = offset.x, y = offset.y)
                                        )
                                    )
                                    return@forEachIndexed
                                }
                                startAngle += sweepAngle
                            }
                        } else {
                            setTooltipInfo(null)
                        }
                    }
                }
        ) {
            canvasSize = size
            var startAngle = -90f
            data.forEachIndexed { index, pieData ->
                val sweepAngle = animatedSweepAngles.getOrNull(index) ?: 0f
                drawArc(
                    color = pieData.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset.Zero,
                    size = size,
                    style = Fill
                )
                startAngle += sweepAngle
            }
        }

        // Add a legend
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            data.forEach { pieData ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(pieData.color, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = pieData.serviceName,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        tooltipInfo?.let { tooltip ->
            Tooltip(
                text = tooltip.text,
                position = tooltip.position,
                parentSize = canvasSize
            )
        }
    }
}
