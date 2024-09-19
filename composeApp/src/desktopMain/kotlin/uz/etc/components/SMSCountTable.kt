package uz.etc.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.NumberFormat

@Composable
fun SMSCountsTable(counts: Map<String, Int>) {
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        "SMS Counts by Service",
        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(8.dp))

    // Format counts for easier readability
    val numberFormat = NumberFormat.getInstance()

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
                    .background(if (counts.keys.indexOf(service) % 2 == 0) MaterialTheme.colors.onSurface.copy(alpha = 0.05f) else MaterialTheme.colors.background)
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
