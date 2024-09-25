// src/main/kotlin/uz/etc/components/MainContent.kt
package uz.etc.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.etc.models.FileResult
import java.awt.Window

@Composable
fun MainContent(
    window: Window,
    fileResults: List<FileResult>,
    selectedFileIndex: Int?,
    onFileSelected: (java.io.File) -> Unit
) {
    var currentView by remember { mutableStateOf("Table") } // Track the current view

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        if (selectedFileIndex != null && selectedFileIndex < fileResults.size) {
            val selectedFileResult = fileResults[selectedFileIndex]

            Text(
                "File: ${selectedFileResult.fileName}",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )

            // View Switch Buttons
            Row(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Button(
                    onClick = { currentView = "Table" },
                    enabled = currentView != "Table"
                ) {
                    Text("Table View")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { currentView = "Analytics" },
                    enabled = currentView != "Analytics"
                ) {
                    Text("Analytics View")
                }
            }

            // Display the selected view
            when (currentView) {
                "Table" -> SMSCountsTable(selectedFileResult.counts)
                "Analytics" -> AnalyticsView(
                    counts = selectedFileResult.counts
                )
            }
        } else if (fileResults.isNotEmpty()) {
            Text(
                "Select a file from the list to view its results.",
                style = MaterialTheme.typography.h6
            )
        } else {
            FileDropArea(
                window = window,
                onFileSelected = onFileSelected
            )
        }
    }
}
