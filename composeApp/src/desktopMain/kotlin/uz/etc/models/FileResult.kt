package uz.etc.models

data class FileResult(
    val fileName: String,
    val counts: Map<String, Int>
)
