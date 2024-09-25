package uz.etc.models

import uz.etc.components.LineChartData

data class FileResult(
    val fileName: String,
    val counts: Map<String, Int>,
    val lineChartData: List<LineChartData>
)
