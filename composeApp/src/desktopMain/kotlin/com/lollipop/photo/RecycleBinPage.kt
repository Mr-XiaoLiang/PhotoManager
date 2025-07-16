package com.lollipop.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lollipop.photo.data.SortMode
import com.lollipop.photo.data.SortType
import com.lollipop.photo.recycle.RecycleBinWindowState
import com.lollipop.photo.state.UiController
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.widget.ColumnMenu
import com.lollipop.photo.widget.ContentBox
import com.lollipop.photo.widget.ContentDensityMenuWidget
import com.lollipop.photo.widget.ContentMenuIcon
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.*

@Composable
fun RecycleBinPage(state: RecycleBinWindowState) {
    val recycleBinDialogState = remember { state.recycleBinDialogState }
    AppWindow(
        callClose = state::close,
        title = state.title
    ) { actionBarHeight ->
        RecycleBinDetail(actionBarHeight = actionBarHeight, state = state)
        recycleBinDialogState.DialogCompose()
    }
}

@Composable
private fun RecycleBinDetail(actionBarHeight: Dp, state: RecycleBinWindowState) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val photoList = remember { state.photoList }
        val sortType by remember { state.sortType }
        val sortMode by remember { state.sortMode }
        val isGridMode by remember { state.gridMode }
        val contentDensityMode by remember { state.contentDensityMode }
        val recycleBinDialogState = remember { state.recycleBinDialogState }
        ContentBox(
            topInsets = actionBarHeight,
            modifier = Modifier.fillMaxSize(),
            menuBar = { callExpand ->
                when (sortType) {
                    SortType.Name -> {
                        when (sortMode) {
                            SortMode.Upward -> {
                                ContentMenuIcon(
                                    onClick = { state.updateSortMode(SortMode.Downward) },
                                    painter = painterResource(Res.drawable.icon_text_up_24),
                                    contentDescription = StringsKey.SortTextUpward,
                                    light = true
                                )
                            }

                            SortMode.Downward -> {
                                ContentMenuIcon(
                                    onClick = { state.updateSortMode(SortMode.Upward) },
                                    painter = painterResource(Res.drawable.icon_text_down_24),
                                    contentDescription = StringsKey.SortTextDownward,
                                    light = true
                                )
                            }
                        }
                        ContentMenuIcon(
                            onClick = { state.updateSortType(SortType.Time) },
                            painter = painterResource(Res.drawable.icon_clock_24),
                            contentDescription = StringsKey.SortByTime,
                        )
                    }

                    SortType.Time -> {
                        ContentMenuIcon(
                            onClick = { state.updateSortType(SortType.Name) },
                            painter = painterResource(Res.drawable.icon_text_24),
                            contentDescription = StringsKey.SortByText,
                        )
                        when (sortMode) {
                            SortMode.Upward -> {
                                ContentMenuIcon(
                                    onClick = { state.updateSortMode(SortMode.Downward) },
                                    painter = painterResource(Res.drawable.icon_clock_arrow_up_24),
                                    contentDescription = StringsKey.SortTimeUpward,
                                    light = true
                                )
                            }

                            SortMode.Downward -> {
                                ContentMenuIcon(
                                    onClick = { state.updateSortMode(SortMode.Upward) },
                                    painter = painterResource(Res.drawable.icon_clock_arrow_down_24),
                                    contentDescription = StringsKey.SortTimeDownward,
                                    light = true
                                )
                            }
                        }
                    }
                }
                if (isGridMode) {
                    ContentMenuIcon(
                        onClick = { state.updateGridMode(false) },
                        painter = painterResource(Res.drawable.icon_grid_on_24),
                        contentDescription = StringsKey.GridOn,
                    )
                } else {
                    ContentMenuIcon(
                        onClick = { state.updateGridMode(true) },
                        painter = painterResource(Res.drawable.icon_grid_off_24),
                        contentDescription = StringsKey.GridOff,
                    )
                }
                ContentMenuIcon(
                    onClick = {
                        state.refresh()
                    },
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = StringsKey.Refresh,
                )
                // 展开菜单的按钮
                ContentMenuIcon(
                    onClick = { callExpand() },
                    painter = painterResource(Res.drawable.window_action_menu_24),
                    contentDescription = StringsKey.More,
                )
            },
            menuPanel = { callClose ->
                ColumnMenu(
                    modifier = Modifier.width(320.dp).padding(horizontal = 6.dp, vertical = 6.dp),
                ) {
                    ContentDensityMenuWidget(
                        currentMode = contentDensityMode,
                    ) {
                        UiController.updateContentDensityMode(it)
                    }
                    AppSettings()
                }
            }
        ) { contentTop ->
            PhotoItems(
                isGridMode = isGridMode,
                contentTop = contentTop,
                contentDensityMode = contentDensityMode,
                currentFolder = null,
                isRecycleBin = true,
                photoList = photoList,
                recycleBinDialogState = recycleBinDialogState
            )
        }
    }

}