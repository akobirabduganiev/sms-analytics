// src/main/kotlin/uz/etc/components/AnalyticsView.kt
package uz.etc.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnalyticsView(counts: Map<String, Int>) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Bar Chart", "Pie Chart")

    // Tooltip state
    var tooltipInfo by remember { mutableStateOf<TooltipInfo?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> BarChart(
                data = counts.entries.toList(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                tooltipInfo = tooltipInfo,
                setTooltipInfo = { tooltipInfo = it }
            )
            1 -> PieChart(
                data = generatePieChartData(counts),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                tooltipInfo = tooltipInfo,
                setTooltipInfo = { tooltipInfo = it }
            )
        }
    }
}
