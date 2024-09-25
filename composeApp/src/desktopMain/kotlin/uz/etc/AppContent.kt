package uz.etc.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uz.etc.models.FileResult
import uz.etc.utils.processFile
import java.awt.Window

@Composable
fun AppContent(
    window: Window,
    isProcessing: Boolean,
    fileResults: MutableList<FileResult>,
    progress: Int,
    totalLines: Int,
    selectedFileIndex: Int?,
    coroutineScope: CoroutineScope,
    setIsProcessing: (Boolean) -> Unit,
    setProgress: (Int) -> Unit,
    setTotalLines: (Int) -> Unit,
    setSelectedFileIndex: (Int?) -> Unit
) {
    Column {
        TopAppBar(
            title = { Text("SMS Service Counter", color = Color.White) },
            backgroundColor = MaterialTheme.colors.primary
        )
        Row(modifier = Modifier.fillMaxSize()) {
            // Sidebar
            Sidebar(
                fileResults = fileResults,
                selectedFileIndex = selectedFileIndex,
                isProcessing = isProcessing,
                progress = progress,
                totalLines = totalLines,
                onFileSelected = { index -> setSelectedFileIndex(index) },
                onUploadFile = {
                    coroutineScope.launch {
                        processFile(
                            fileResults = fileResults,
                            setIsProcessing = setIsProcessing,
                            setProgress = setProgress,
                            setTotalLines = setTotalLines,
                            setSelectedFileIndex = setSelectedFileIndex
                        )
                    }
                },
                onResetAll = {
                    fileResults.clear()
                    setSelectedFileIndex(null)
                    setProgress(0)
                    setTotalLines(1)
                }
            )
            Divider(modifier = Modifier.width(1.dp).fillMaxHeight())
            // Main content area
            MainContent(
                window = window,
                fileResults = fileResults,
                selectedFileIndex = selectedFileIndex,
                onFileSelected = { file ->
                    coroutineScope.launch {
                        processFile(
                            fileResults = fileResults,
                            setIsProcessing = setIsProcessing,
                            setProgress = setProgress,
                            setTotalLines = setTotalLines,
                            setSelectedFileIndex = setSelectedFileIndex,
                            file = file
                        )
                    }
                }
            )
        }
    }
}
