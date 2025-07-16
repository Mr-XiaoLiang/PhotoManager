package com.lollipop.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.detail.PhotoDetailWindowManager
import com.lollipop.photo.recycle.RecycleBinWindowManager
import com.lollipop.photo.state.WindowConfig
import com.lollipop.photo.state.WindowStateController
import com.lollipop.photo.values.Strings

fun main() = application {

    PhotoManager.init()
    Strings.init()

    val photoDetailWindows = remember { PhotoDetailWindowManager.windows }
    val recycleBinWindows = remember { RecycleBinWindowManager.windows }

//    val keepOnBackground by remember { WindowConfig.keepOnBackground }

//    Tray(
//        icon = painterResource(Res.drawable.icon_clock_24),
//        menu = {
//            Item(text = "Keep on background", onClick = { WindowConfig.updateKeepOnBackground(true) })
//            Item(text = "Quit on close", onClick = { WindowConfig.updateKeepOnBackground(false) })
//            Item("Quit", onClick = ::exitApplication)
//        }
//    )

    MainWindow()

    for (window in photoDetailWindows) {
        key(window) {
            PhotoDetailPage(window)
        }
    }

    for (window in recycleBinWindows) {
        key(window) {
            RecycleBinPage(window)
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ApplicationScope.MainWindow() {
    val titleState by remember { WindowStateController.windowTitle }
    val keepOnBackground by remember { WindowConfig.keepOnBackground }
    AppWindow(
        callClose = {
            if (!keepOnBackground) {
                exitApplication()
            }
        },
        title = titleState
    ) { actionBarHeight ->
        App(actionBarHeight)
        RecycleBinDialogState.Main.DialogCompose()
    }
}
