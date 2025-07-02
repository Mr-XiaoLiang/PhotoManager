package com.lollipop.photo

import androidx.compose.ui.window.application
import com.lollipop.photo.widget.RoundWindow

fun main() = application {
    RoundWindow(
        title = "PhotoManager",
    ) {
        App()
    }
}