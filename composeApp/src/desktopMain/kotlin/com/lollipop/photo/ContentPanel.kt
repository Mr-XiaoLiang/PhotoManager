package com.lollipop.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lollipop.photo.state.WindowStateController
import com.lollipop.photo.widget.ContentBox
import com.lollipop.photo.widget.ContentMenuIcon
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.*

@Composable
fun ContentPanel(
    modifier: Modifier,
    topInsets: Dp
) {
    val isDrawerExpand by remember { WindowStateController.drawerExpand }
    ContentBox(
        modifier = modifier,
        menuBar = { callExpand ->
            if (isDrawerExpand) {
                ContentMenuIcon(
                    onClick = { WindowStateController.updateDrawerExpand(false) },
                    painter = painterResource(Res.drawable.icon_left_panel_close_24),
                    contentDescription = "CloseDrawer",
                )
            } else {
                ContentMenuIcon(
                    onClick = { WindowStateController.updateDrawerExpand(true) },
                    painter = painterResource(Res.drawable.icon_left_panel_open_24),
                    contentDescription = "OpenDrawer",
                )
            }
            // 展开菜单的按钮
            ContentMenuIcon(
                onClick = { callExpand() },
                painter = painterResource(Res.drawable.window_action_menu_24),
                contentDescription = "Close",
            )
        },
        menuPanel = { callClose ->
            Box(modifier = Modifier.width(240.dp).height(240.dp)) {
                ContentMenuIcon(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        callClose()
                    },
                    painter = painterResource(Res.drawable.window_action_close_24),
                    contentDescription = "Close",
                )
            }
        }
    ) {
    }
    // TODO
}