package uz.etc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uz.etc.models.FileResult

@Composable
fun Sidebar(
    fileResults: List<FileResult>,
    selectedFileIndex: Int?,
    isProcessing: Boolean,
    progress: Int,
    totalLines: Int,
    onFileSelected: (Int) -> Unit,
    onUploadFile: () -> Unit,
    onResetAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {
        Text(
            "Processed Files",
            style = MaterialTheme.typography.h6.copy(fontWeight = MaterialTheme.typography.h6.fontWeight),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (fileResults.isEmpty()) {
            Text("No files processed yet.")
        } else {
            // List of processed files
            LazyColumn {
                itemsIndexed(fileResults) { index, fileResult ->
                    FileItem(
                        fileResult = fileResult,
                        isSelected = index == selectedFileIndex,
                        onClick = { onFileSelected(index) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // "Upload File" Button
        Button(
            onClick = onUploadFile,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Icon(imageVector = Icons.Default.UploadFile, contentDescription = "Upload")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Upload File", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        // "Reset" Button to clear all data
        Button(
            onClick = onResetAll,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Reset")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset All", color = MaterialTheme.colors.onError)
        }
        if (isProcessing) {
            Spacer(modifier = Modifier.height(16.dp))
            ProcessingIndicator(progress, totalLines)
        }
        Spacer(modifier = Modifier.weight(1f))
        // Copyright Notice
        Text(
            "© ET-SW Team",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.DarkGray
        )
    }
}
