package com.lollipop.photo

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.state.WindowStateController
import com.lollipop.photo.widget.AppWindowActionWidget
import com.lollipop.photo.widget.RoundWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {

    PhotoManager.init()

    val dragAreaHeight = 32.dp
    val actionBarHeight = dragAreaHeight + 12.dp
    val actionBarMargin = 6.dp
    val titleState by remember { WindowStateController.windowTitle }
    var isPointerHold by remember { mutableStateOf(false) }
    val windowControllerElevation by animateDpAsState(
        targetValue = if (isPointerHold) {
            12.dp
        } else {
            0.dp
        },
    )
    RoundWindow { windowState ->
        MaterialTheme {
            App(actionBarHeight)
            Box(
                modifier = Modifier.fillMaxWidth().height(actionBarHeight).padding(horizontal = actionBarMargin),
                contentAlignment = Alignment.CenterStart
            ) {

                WindowDraggableArea(
                    modifier = Modifier.height(dragAreaHeight)
                        .onPointerEvent(PointerEventType.Enter) { isPointerHold = true }
                        .onPointerEvent(PointerEventType.Exit) { isPointerHold = false }
                ) {
                    Card(
                        elevation = windowControllerElevation,
                        backgroundColor = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(dragAreaHeight)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp)
                                .fillMaxHeight()
                                .animateContentSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppWindowActionWidget(windowState = windowState)
                            if (isPointerHold) {
                                Text(
                                    text = titleState,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                        .horizontalScroll(rememberScrollState()),
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}