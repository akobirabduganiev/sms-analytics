// src/main/kotlin/uz/etc/components/DataModels.kt
package uz.etc.components

import androidx.compose.ui.graphics.Color

data class PieChartData(
    val serviceName: String,
    val count: Int,
    val color: Color
)

data class LineChartData(
    val serviceName: String,
    val countsOverTime: List<Int>,
    val color: Color
)

fun generatePieChartData(counts: Map<String, Int>): List<PieChartData> {
    val colors = listOf(
        Color(0xFF1E88E5),
        Color(0xFFD81B60),
        Color(0xFF43A047),
        Color(0xFFFFB300),
        Color(0xFF8E24AA),
        Color(0xFF00ACC1),
        Color(0xFFD50000),
        Color(0xFF7CB342),
        Color(0xFFFF7043),
        Color(0xFF3949AB),
        Color(0xFFD32F2F),
        Color(0xFFC2185B),
        Color(0xFF7B1FA2),
        Color(0xFF303F9F),
        Color(0xFF1976D2),
        Color(0xFF0288D1),
        Color(0xFF0097A7),
        // Add more colors if needed
    )
    return counts.entries.mapIndexed { index, entry ->
        PieChartData(
            serviceName = entry.key,
            count = entry.value,
            color = colors[index % colors.size]
        )
    }
}

fun generateLineChartData(counts: Map<String, List<Int>>): List<LineChartData> {
    val colors = listOf(
        Color(0xFF1E88E5),
        Color(0xFFD81B60),
        Color(0xFF43A047),
        Color(0xFFFFB300),
        Color(0xFF8E24AA),
        Color(0xFF00ACC1),
        Color(0xFFD50000),
        Color(0xFF7CB342),
        Color(0xFFFF7043),
        Color(0xFF3949AB),
        Color(0xFFD32F2F),
        Color(0xFFC2185B),
        Color(0xFF7B1FA2),
        Color(0xFF303F9F),
        Color(0xFF1976D2),
        Color(0xFF0288D1),
        Color(0xFF0097A7),
    )
    return counts.entries.mapIndexed { index, entry ->
        LineChartData(
            serviceName = entry.key,
            countsOverTime = entry.value,
            color = colors[index % colors.size]
        )
    }
}
