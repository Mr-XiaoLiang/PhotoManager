package com.lollipop.photo.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.*

@Stable
interface WindowFrameScope {

    val actionBarHeight: Dp
    val actionBarWidth: Dp?

}

@Composable
fun ApplicationScope.RoundWindow(
    title: String,
    enableCloseAction: Boolean = true,
    enableMinimizeAction: Boolean = true,
    enableMaximizeAction: Boolean = true,
    enableMenuAction: Boolean = false,
    actionBarWidth: Dp? = null,
    onMenuClick: () -> Unit = {},
    content: @Composable WindowFrameScope.() -> Unit
) {
    val windowState = rememberWindowState()
    var isMaximized by remember { mutableStateOf(false) }
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = title,
        undecorated = true,
        transparent = true
    ) {
        WindowFrame(
            title = title,
            actionBarWidth = actionBarWidth,
            onExit = {
                exitApplication()
            },
            onMinimize = {
                windowState.isMinimized = true
            },
            onMaximize = {
                if (windowState.placement == WindowPlacement.Maximized) {
                    windowState.placement = WindowPlacement.Floating
                    isMaximized = false
                } else {
                    windowState.placement = WindowPlacement.Maximized
                    isMaximized = true
                }
            },
            isMaximized = isMaximized,
            enableCloseAction = enableCloseAction,
            enableMinimizeAction = enableMinimizeAction,
            enableMaximizeAction = enableMaximizeAction,
            enableMenuAction = enableMenuAction,
            onMenuClick = onMenuClick
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WindowScope.WindowFrame(
    title: String,
    onExit: () -> Unit = {},
    onMinimize: () -> Unit = {},
    onMaximize: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    enableCloseAction: Boolean = true,
    enableMinimizeAction: Boolean = true,
    enableMaximizeAction: Boolean = true,
    enableMenuAction: Boolean = false,
    isMaximized: Boolean = false,
    radius: Dp = 12.dp,
    actionBarWidth: Dp? = null,
    actionBarHeight: Dp = 32.dp,
    fixInsets: Boolean = false,
    menuIcon: Painter = painterResource(Res.drawable.window_action_menu_24),
    content: @Composable WindowFrameScope.() -> Unit
) {

    val actionSize = 24.dp
    val menuButtonSize = if (actionBarHeight > actionSize) {
        actionSize
    } else {
        actionBarHeight
    }
    val actionBarPadding = if (actionBarHeight > actionSize) {
        (actionBarHeight - actionSize) / 2
    } else {
        0.dp
    }

    val windowInsets = if (fixInsets) {
        actionBarHeight
    } else {
        0.dp
    }

    val actionBarHeightSize = actionBarHeight
    val actionBarWidthSize = actionBarWidth

    val windowScope = object : WindowFrameScope {
        override val actionBarHeight: Dp = actionBarHeightSize
        override val actionBarWidth: Dp? = actionBarWidthSize
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .clip(shape = RoundedCornerShape(radius))
            .background(MaterialTheme.colors.background)
            .padding(top = windowInsets)
    ) {
        windowScope.content()
    }

    WindowDraggableArea {
        val actionBarModifier = if (actionBarWidth != null) {
            Modifier.width(actionBarWidth)
        } else {
            Modifier.fillMaxWidth()
        }
        Row(
            modifier = actionBarModifier.height(actionBarHeight).padding(horizontal = actionBarPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WindowActionWidget(
                onExit = onExit,
                onMinimize = onMinimize,
                onMaximize = onMaximize,
                isMaximized = isMaximized,
                enableCloseAction = enableCloseAction,
                enableMinimizeAction = enableMinimizeAction,
                enableMaximizeAction = enableMaximizeAction
            )
            Text(
                modifier = Modifier.weight(1F).padding(horizontal = 12.dp),
                text = title,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onBackground
            )

            if (enableMenuAction) {
                Icon(
                    modifier = Modifier.width(menuButtonSize)
                        .height(menuButtonSize)
                        .onClick(onClick = onMenuClick),
                    painter = menuIcon,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onBackground
                )
            }

        }
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun WindowActionWidget(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
    onMinimize: () -> Unit = {},
    onMaximize: () -> Unit = {},
    isMaximized: Boolean = false,
    closeIcon: Painter = painterResource(Res.drawable.window_action_close_24),
    minimizeIcon: Painter = painterResource(Res.drawable.window_action_minimize_24),
    maximizeIcon: Painter = painterResource(Res.drawable.window_action_maximize_24),
    floatingIcon: Painter? = painterResource(Res.drawable.window_action_floating_24),
    disableColor: Color = Color(0xDD9A9A9A.toInt()),
    closeColor: Color = Color(0xFFE04040.toInt()),
    closeIconColor: Color = Color(0xFFB13636.toInt()),
    minimizeColor: Color = Color(0xFFD9C539.toInt()),
    minimizeIconColor: Color = Color(0xFFB6A632.toInt()),
    maximizeColor: Color = Color(0xFF3AD38D.toInt()),
    maximizeIconColor: Color = Color(0xFF2FAA72.toInt()),
    enableCloseAction: Boolean = true,
    enableMinimizeAction: Boolean = true,
    enableMaximizeAction: Boolean = true,
) {

    val iconSize = 24.dp
    val iconPadding = 4.dp
    val iconInsets = 2.dp

    var isPointerEnter by remember { mutableStateOf(false) }

    val closeBtnColor = if (isPointerEnter && enableCloseAction) {
        closeColor
    } else {
        disableColor
    }
    val closeIconTint = if (isPointerEnter && enableCloseAction) {
        closeIconColor
    } else {
        disableColor
    }

    val minimizeBtnColor = if (isPointerEnter && enableMinimizeAction) {
        minimizeColor
    } else {
        disableColor
    }
    val minimizeIconTint = if (isPointerEnter && enableMinimizeAction) {
        minimizeIconColor
    } else {
        disableColor
    }

    val maximizeBtnColor = if (isPointerEnter && enableMaximizeAction) {
        maximizeColor
    } else {
        disableColor
    }
    val maximizeIconTint = if (isPointerEnter && enableMaximizeAction) {
        maximizeIconColor
    } else {
        disableColor
    }


    Row(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { isPointerEnter = true }
            .onPointerEvent(PointerEventType.Exit) { isPointerEnter = false },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.width(iconSize)
                .height(iconSize)
                .onClick(enabled = enableCloseAction, onClick = onExit)
                .padding(iconPadding)
                .background(color = closeBtnColor, shape = RoundedCornerShape(iconSize))
                .padding(iconInsets),
            painter = closeIcon,
            contentDescription = "Close",
            tint = closeIconTint
        )

        Icon(
            modifier = Modifier.width(iconSize)
                .height(iconSize)
                .onClick(enabled = enableMinimizeAction, onClick = onMinimize)
                .padding(iconPadding)
                .background(color = minimizeBtnColor, shape = RoundedCornerShape(iconSize))
                .padding(iconInsets),
            painter = minimizeIcon,
            contentDescription = "Minimize",
            tint = minimizeIconTint
        )

        val maximizeIconPainter = if (isMaximized || floatingIcon == null) {
            maximizeIcon
        } else {
            floatingIcon
        }

        Icon(
            modifier = Modifier.width(iconSize)
                .height(iconSize)
                .onClick(enabled = enableMaximizeAction, onClick = onMaximize)
                .padding(iconPadding)
                .background(color = maximizeBtnColor, shape = RoundedCornerShape(iconSize))
                .padding(iconInsets),
            painter = maximizeIconPainter,
            contentDescription = "Maximize",
            tint = maximizeIconTint
        )

    }

}

