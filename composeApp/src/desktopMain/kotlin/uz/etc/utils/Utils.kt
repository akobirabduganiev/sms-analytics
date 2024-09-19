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
