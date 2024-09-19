package uz.etc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.awt.dnd.*
import java.io.File

@Composable
fun FileDropArea(
    window: java.awt.Window,
    onFileSelected: (File) -> Unit
) {
    val isHovered = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val dropTargetListener = object : DropTargetAdapter() {
            override fun dragEnter(dtde: DropTargetDragEvent?) {
                isHovered.value = true
            }

            override fun dragExit(dte: DropTargetEvent?) {
                isHovered.value = false
            }

            override fun drop(dtde: DropTargetDropEvent) {
                isHovered.value = false
                dtde.acceptDrop(DnDConstants.ACTION_COPY)
                val transferable = dtde.transferable
                val dataFlavors = transferable.transferDataFlavors
                dataFlavors.forEach { flavor ->
                    if (flavor.isFlavorJavaFileListType) {
                        val files = transferable.getTransferData(flavor) as List<*>
                        val file = files.firstOrNull() as? File
                        if (file != null && file.extension == "txt") {
                            coroutineScope.launch {
                                onFileSelected(file)
                            }
                        }
                    }
                }
                dtde.dropComplete(true)
            }
        }

        val dropTarget = DropTarget(window, dropTargetListener)

        onDispose {
            dropTarget.removeDropTargetListener(dropTargetListener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .border(
                width = 2.dp,
                color = if (isHovered.value) MaterialTheme.colors.primary else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color(0xFFEFEFEF))
            .clickable {
                coroutineScope.launch {
                    val file = uz.etc.utils.openFileChooser()
                    if (file != null) {
                        onFileSelected(file)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = "Upload Icon",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Drag and drop a text file here",
                style = MaterialTheme.typography.h6
            )
            Text(
                "or",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                "Click to select a file",
                style = MaterialTheme.typography.button.copy(fontWeight = MaterialTheme.typography.button.fontWeight),
                color = MaterialTheme.colors.primary
            )
        }
    }
}
