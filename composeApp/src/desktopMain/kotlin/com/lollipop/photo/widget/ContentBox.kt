package com.lollipop.photo.widget

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.Res
import photomanager.composeapp.generated.resources.window_action_menu_24

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ContentBox(
    modifier: Modifier = Modifier,
    menuBar: @Composable (RowScope.() -> Unit)? = null,
    menuPanel: @Composable (BoxScope.(callClose: () -> Unit) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val menuButtonSize = 32.dp
    val isShowMore = menuPanel != null
    val isShowMenu = menuBar != null || isShowMore
    Box(modifier) {
        content()
        if (isShowMenu) {
            Box(
                modifier.fillMaxSize().padding(8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Card(
                    modifier = Modifier,
                    elevation = 6.dp,
                    backgroundColor = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(menuButtonSize / 2)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 4.dp)
                            .defaultMinSize(minWidth = menuButtonSize, minHeight = menuButtonSize)
                            .animateContentSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        if (isMenuExpanded && isShowMore) {
                            menuPanel {
                                isMenuExpanded = false
                            }
                        } else {
                            Row(
                                modifier = Modifier.height(menuButtonSize),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                menuBar?.invoke(this)
                                if (isShowMore) {
                                    ContentMenuIcon(
                                        onClick = {
                                            isMenuExpanded = !isMenuExpanded
                                        },
                                        painter = painterResource(Res.drawable.window_action_menu_24),
                                        contentDescription = "Menu",
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ContentMenuIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String = "",
    wider: Boolean = false,
    onClick: () -> Unit,
) {
    val menuButtonHeight = 32.dp
    val menuButtonWidth = if (wider) {
        menuButtonHeight + 8.dp
    } else {
        menuButtonHeight
    }
    Icon(
        modifier = modifier.width(menuButtonWidth)
            .height(menuButtonHeight)
            .onClick(onClick = onClick)
            .padding(
                vertical = 4.dp,
                horizontal = if (wider) {
                    8.dp
                } else {
                    4.dp
                }
            ),
        painter = painter,
        contentDescription = contentDescription,
        tint = MaterialTheme.colors.onSurface
    )
}
