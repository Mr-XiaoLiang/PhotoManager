package com.lollipop.photo

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lollipop.photo.state.WindowStateController

@Composable
fun App(actionBarHeight: Dp) {
    val isDrawerExpand by remember { WindowStateController.drawerExpand }
    val drawerWidth by animateDpAsState(
        targetValue = if (isDrawerExpand) {
            240.dp
        } else {
            0.dp
        }
    )
    Row(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
    ) {
        DrawerPanel(
            modifier = Modifier.fillMaxHeight()
                .animateContentSize()
                .width(drawerWidth)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            topInsets = actionBarHeight
        )
        ContentPanel(
            modifier = Modifier.fillMaxSize().animateContentSize(),
            topInsets = actionBarHeight
        )
    }
}