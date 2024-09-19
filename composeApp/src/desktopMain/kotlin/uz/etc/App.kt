package uz.etc

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import uz.etc.components.AppContent
import uz.etc.models.FileResult

@Composable
fun App(window: java.awt.Window) {
    var isProcessing by remember { mutableStateOf(false) }
    val fileResults = remember { mutableStateListOf<FileResult>() }
    var progress by remember { mutableStateOf(0) }
    var totalLines by remember { mutableStateOf(1) }
    var selectedFileIndex by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()

    MaterialTheme(
        colors = lightColors(
            primary = Color(0xFF6200EE),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC6)
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            AppContent(
                window = window,
                isProcessing = isProcessing,
                fileResults = fileResults,
                progress = progress,
                totalLines = totalLines,
                selectedFileIndex = selectedFileIndex,
                coroutineScope = coroutineScope,
                setIsProcessing = { isProcessing = it },
                setProgress = { progress = it },
                setTotalLines = { totalLines = it },
                setSelectedFileIndex = { selectedFileIndex = it }
            )
        }
    }
}
