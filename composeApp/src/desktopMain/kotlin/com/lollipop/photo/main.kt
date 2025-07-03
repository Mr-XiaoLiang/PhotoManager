package com.lollipop.photo

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.lollipop.photo.widget.RoundWindow

fun main() = application {
    RoundWindow(
        title = "PhotoManager",
        actionBarWidth = 240.dp,
    ) {
        App()
    }
}