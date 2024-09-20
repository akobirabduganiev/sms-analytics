package uz.etc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import uz.etc.utils.saveFileDialog
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.text.NumberFormat

@Composable
fun SMSCountsTable(counts: Map<String, Int>) {
    val numberFormat = NumberFormat.getInstance()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() } // Define snackbarHostState here

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "SMS Counts by Service",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        // Copy to Clipboard
                        val tableText = buildTableText(counts)
                        val stringSelection = StringSelection(tableText)
                        Toolkit.getDefaultToolkit().systemClipboard.setContents(stringSelection, null)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Data copied to clipboard")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Copy to Clipboard", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        // Export to File
                        coroutineScope.launch {
                            val file = saveFileDialog()
                            if (file != null) {
                                try {
                                    withContext(Dispatchers.IO) {
                                        file.writeText(buildTableText(counts))
                                    }
                                    snackbarHostState.showSnackbar("File saved: ${file.name}")
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    snackbarHostState.showSnackbar("Error saving file: ${e.message}")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Export as Text", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    // Header Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.primaryVariant)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Service Identifier",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "SMS Count",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                    Divider()
                }

                items(counts.entries.sortedByDescending { it.value }) { (service, count) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(
                                if (counts.keys.indexOf(service) % 2 == 0)
                                    MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
                                else
                                    MaterialTheme.colors.background
                            )
                    ) {
                        Text(
                            text = service,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.body1
                        )
                        Text(
                            text = numberFormat.format(count),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Divider()
                }
            }
        }

        // Place SnackbarHost at the appropriate place in the layout
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// Helper function to build table text
private fun buildTableText(counts: Map<String, Int>): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append("Service Identifier\tSMS Count\n")
    stringBuilder.append("----------------------------------\n")
    counts.entries.sortedByDescending { it.value }.forEach { (service, count) ->
        stringBuilder.append("$service\t$count\n")
    }
    return stringBuilder.toString()
}
