package com.lollipop.photo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.lollipop.photo.widget.RoundWindow

fun main() = application {
    RoundWindow(
        title = "PhotoManager",
    ) {
        App()
    }
}