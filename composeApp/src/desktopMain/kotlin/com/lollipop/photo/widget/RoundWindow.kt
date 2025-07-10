package com.lollipop.photo.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState

@Composable
fun RoundWindow(
    radius: Dp = 12.dp,
    callClose: () -> Unit,
    content: @Composable WindowScope.(WindowState) -> Unit
) {
    val windowState = rememberWindowState()
    Window(
        onCloseRequest = callClose,
        state = windowState,
        undecorated = true,
        transparent = true
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .clip(shape = RoundedCornerShape(radius))
                .background(MaterialTheme.colors.background)
        ) {
            content(windowState)
        }
    }
}


