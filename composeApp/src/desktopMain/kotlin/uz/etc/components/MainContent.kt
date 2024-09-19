package uz.etc.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.etc.models.FileResult
import java.awt.Window

@Composable
fun MainContent(
    window: Window,
    fileResults: List<FileResult>,
    selectedFileIndex: Int?,
    isProcessing: Boolean,
    progress: Int,
    totalLines: Int,
    onFileSelected: (java.io.File) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        if (selectedFileIndex != null && selectedFileIndex < fileResults.size) {
            val selectedFileResult = fileResults[selectedFileIndex]
            Text(
                "File: ${selectedFileResult.fileName}",
                style = MaterialTheme.typography.h6.copy(fontWeight = MaterialTheme.typography.h6.fontWeight),
                modifier = Modifier.weight(1f)
            )
            SMSCountsTable(selectedFileResult.counts)
        } else if (fileResults.isNotEmpty()) {
            Text("Select a file from the list to view its results.", style = MaterialTheme.typography.h6)
        } else {
            FileDropArea(
                window = window,
                onFileSelected = onFileSelected
            )
        }
    }
}