package uz.etc.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.etc.models.FileResult
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.LongAdder
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope

suspend fun processFile(
    fileResults: MutableList<FileResult>,
    setIsProcessing: (Boolean) -> Unit,
    setProgress: (Int) -> Unit,
    setTotalLines: (Int) -> Unit,
    setSelectedFileIndex: (Int?) -> Unit,
    file: File? = null
) {
    setIsProcessing(true)
    setProgress(0)
    setTotalLines(1)
    try {
        val selectedFile = file ?: openFileChooser()
        if (selectedFile != null) {
            val counts = processTextFile(selectedFile,
                onProgressUpdate = { processedLines, total ->
                    setProgress(processedLines)
                    setTotalLines(total)
                }
            )
            fileResults.add(FileResult(selectedFile.name, counts))
            setSelectedFileIndex(fileResults.size - 1)
        }
    } catch (e: Exception) {
        println("Error during processing: ${e.message}")
        e.printStackTrace()
    } finally {
        setIsProcessing(false)
    }
}

suspend fun processTextFile(
    file: File,
    onProgressUpdate: suspend (processedLines: Int, totalLines: Int) -> Unit
): Map<String, Int> = withContext(Dispatchers.IO) {
    val serviceCounts = ConcurrentHashMap<String, LongAdder>()
    val totalLines = file.useLines { it.count() }

    if (totalLines == 0) {
        // No lines to process
        onProgressUpdate(0, 0)
        return@withContext emptyMap()
    }

    val availableProcessors = Runtime.getRuntime().availableProcessors()
    val numWorkers = minOf(availableProcessors, totalLines)
    val progressCounter = AtomicInteger(0)
    val channel = Channel<String>(capacity = 1000)

    coroutineScope {
        // Producer coroutine: Reads lines from the file and sends them to the channel
        val producer = launch {
            file.useLines { linesSequence ->
                linesSequence.forEach { line ->
                    channel.send(line)
                }
            }
            channel.close()
        }

        // Worker coroutines: Process lines received from the channel
        val workers = List(numWorkers) {
            launch {
                for (line in channel) {
                    if (line.contains("START_TIME") || line.contains("---")) {
                        val processedLines = progressCounter.incrementAndGet()
                        if (processedLines % 100_000 == 0) {
                            onProgressUpdate(processedLines, totalLines)
                        }
                        continue // Skip header or separator lines
                    }

                    val serviceIdentifier = extractServiceIdentifier(line)
                    if (serviceIdentifier != null && serviceIdentifier.isNotEmpty()) {
                        serviceCounts.computeIfAbsent(serviceIdentifier) { LongAdder() }.increment()
                    }

                    val processedLines = progressCounter.incrementAndGet()
                    if (processedLines % 100_000 == 0) {
                        onProgressUpdate(processedLines, totalLines)
                    }
                }
            }
        }

        // Wait for all coroutines to complete
        producer.join()
        workers.forEach { it.join() }
    }

    // Final progress update to ensure the progress bar reaches 100%
    onProgressUpdate(totalLines, totalLines)

    // Convert LongAdder values to Int
    val counts = serviceCounts.mapValues { it.value.sum().toInt() }

    counts
}

fun extractServiceIdentifier(line: String): String? {
    val columns = line.split("|").map { it.trim() }

    if (columns.size < 4) return null

    // Adjust the index based on the actual position of the service identifier
    return columns[3] // Service Identifier is at index 1
}
