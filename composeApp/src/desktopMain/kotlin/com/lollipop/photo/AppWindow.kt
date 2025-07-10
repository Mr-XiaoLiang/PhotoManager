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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lollipop.photo.state.WindowConfig
import com.lollipop.photo.widget.AppWindowActionWidget
import com.lollipop.photo.widget.RoundWindow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppWindow(
    title: String,
    callClose: () -> Unit,
    content: @Composable (actionBarHeight: Dp) -> Unit
) {
    val dragAreaHeight = WindowConfig.dragAreaHeight
    val actionBarHeight = WindowConfig.actionBarHeight
    val actionBarMargin = WindowConfig.actionBarMargin

    var isPointerHold by remember { mutableStateOf(false) }
    val windowControllerElevation by animateDpAsState(
        targetValue = if (isPointerHold) {
            12.dp
        } else {
            0.dp
        },
    )
    RoundWindow(
        callClose = callClose
    ) { windowState ->
        MaterialTheme {
            content(actionBarHeight)
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
                            AppWindowActionWidget(callClose = callClose, windowState = windowState)
                            if (isPointerHold) {
                                Text(
                                    text = title,
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
