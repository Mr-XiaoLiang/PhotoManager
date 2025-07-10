package com.lollipop.photo.widget

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.lollipop.photo.state.WindowConfig
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.Res
import photomanager.composeapp.generated.resources.window_action_menu_24

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ContentBox(
    topInsets: Dp,
    modifier: Modifier = Modifier,
    menuBar: @Composable (RowScope.(callExpand: () -> Unit) -> Unit)? = null,
    menuPanel: @Composable (BoxScope.(callClose: () -> Unit) -> Unit)? = null,
    content: @Composable (contentTop: Dp) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val menuButtonSize = WindowConfig.menuButtonHeight
    val isShowMore = menuPanel != null
    val isShowMenu = menuBar != null
    Box(modifier) {
        content(max(WindowConfig.menuBarHeight, topInsets))
        if (isMenuExpanded) {
            Box(modifier = Modifier.fillMaxSize().onClick(onClick = { isMenuExpanded = false }))
        }
        if (isShowMenu || isShowMore) {
            Box(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Card(
                    modifier = Modifier,
                    elevation = 6.dp,
                    backgroundColor = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(menuButtonSize / 2)
                ) {
                    Box(
                        modifier = Modifier
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
                                if (isShowMenu) {
                                    menuBar {
                                        isMenuExpanded = true
                                    }
                                } else {
                                    ContentMenuIcon(
                                        onClick = {
                                            isMenuExpanded = true
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
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    contentDescription: String = "",
    light: Boolean = false,
    onClick: () -> Unit,
) {
    val menuButtonHeight = WindowConfig.menuButtonHeight
    val menuButtonWidth = menuButtonHeight
    val tint = if (light) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.onSurface
    }
    IconButton(
        modifier = modifier.width(menuButtonWidth)
            .height(menuButtonHeight),
        onClick = onClick
    ) {
        if (painter != null) {
            Icon(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        vertical = 4.dp,
                        horizontal = 4.dp
                    ),
                painter = painter,
                contentDescription = contentDescription,
                tint = tint
            )
        } else if (imageVector != null) {
            Icon(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        vertical = 4.dp,
                        horizontal = 4.dp
                    ),
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = tint
            )
        }
    }
}

@Composable
fun ColumnMenu(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnMenuButton(
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    label: String = "",
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.onClick(onClick = onClick).defaultMinSize(minHeight = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (painter != null) {
            Icon(
                painter = painter,
                contentDescription = label,
                modifier = Modifier.width(24.dp).height(24.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(24.dp).height(24.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1F),
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnIconMenuButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    label: String = "",
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.onClick(onClick = onClick).defaultMinSize(minHeight = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = label,
                modifier = Modifier.width(24.dp).height(24.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(24.dp).height(24.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1F),
            fontSize = 16.sp
        )
    }
}

