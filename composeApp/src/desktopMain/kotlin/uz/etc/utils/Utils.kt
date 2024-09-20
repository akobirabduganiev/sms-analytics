package uz.etc.utils

import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

fun openFileChooser(): File? {
    val chooser = JFileChooser()
    chooser.fileFilter = FileNameExtensionFilter("Text Files", "txt")
    val result = chooser.showOpenDialog(null)
    return if (result == JFileChooser.APPROVE_OPTION) {
        chooser.selectedFile
    } else {
        null
    }
}

fun saveFileDialog(): File? {
    val chooser = JFileChooser()
    chooser.fileFilter = FileNameExtensionFilter("Text Files", "txt")
    chooser.dialogTitle = "Save As"
    val result = chooser.showSaveDialog(null) // Must be called on EDT
    return if (result == JFileChooser.APPROVE_OPTION) {
        var file = chooser.selectedFile
        if (!file.name.endsWith(".txt")) {
            file = File(file.absolutePath + ".txt")
        }
        file
    } else {
        null
    }
}

