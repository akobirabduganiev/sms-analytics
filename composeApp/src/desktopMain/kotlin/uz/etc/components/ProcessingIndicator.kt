package uz.etc.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProcessingIndicator(progress: Int, totalLines: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = progress / totalLines.toFloat(),
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Processing file... ($progress / $totalLines lines)", style = MaterialTheme.typography.subtitle2)
    }
}
