package com.lollipop.photo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.data.SortMode
import com.lollipop.photo.data.SortType
import com.lollipop.photo.state.UiController
import com.lollipop.photo.state.WindowStateController
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.widget.*
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.*

@Composable
fun ContentPanel(
    modifier: Modifier,
    topInsets: Dp
) {
    val isDrawerExpand by remember { WindowStateController.drawerExpand }
    val photoList = remember { PhotoManager.photoList }
    val currentFolder by remember { PhotoManager.selectedFolder }
    val sortType by remember { PhotoManager.sortType }
    val sortMode by remember { PhotoManager.sortMode }
    val isGridMode by remember { UiController.gridMode }
    val contentDensityMode by remember { UiController.contentDensityMode }
    ContentBox(
        topInsets = topInsets,
        modifier = modifier,
        menuBar = { callExpand ->
            if (isDrawerExpand) {
                ContentMenuIcon(
                    onClick = { WindowStateController.updateDrawerExpand(false) },
                    painter = painterResource(Res.drawable.icon_left_panel_close_24),
                    contentDescription = StringsKey.CloseDrawer,
                )
            } else {
                ContentMenuIcon(
                    onClick = { WindowStateController.updateDrawerExpand(true) },
                    painter = painterResource(Res.drawable.icon_left_panel_open_24),
                    contentDescription = StringsKey.OpenDrawer,
                )
            }
            if (currentFolder != null) {
                when (sortType) {
                    SortType.Name -> {
                        when (sortMode) {
                            SortMode.Upward -> {
                                ContentMenuIcon(
                                    onClick = { PhotoManager.updateSortMode(SortMode.Downward) },
                                    painter = painterResource(Res.drawable.icon_text_up_24),
                                    contentDescription = StringsKey.SortTextUpward,
                                    light = true
                                )
                            }

                            SortMode.Downward -> {
                                ContentMenuIcon(
                                    onClick = { PhotoManager.updateSortMode(SortMode.Upward) },
                                    painter = painterResource(Res.drawable.icon_text_down_24),
                                    contentDescription = StringsKey.SortTextDownward,
                                    light = true
                                )
                            }
                        }
                        ContentMenuIcon(
                            onClick = { PhotoManager.updateSortType(SortType.Time) },
                            painter = painterResource(Res.drawable.icon_clock_24),
                            contentDescription = StringsKey.SortByTime,
                        )
                    }

                    SortType.Time -> {
                        ContentMenuIcon(
                            onClick = { PhotoManager.updateSortType(SortType.Name) },
                            painter = painterResource(Res.drawable.icon_text_24),
                            contentDescription = StringsKey.SortByText,
                        )
                        when (sortMode) {
                            SortMode.Upward -> {
                                ContentMenuIcon(
                                    onClick = { PhotoManager.updateSortMode(SortMode.Downward) },
                                    painter = painterResource(Res.drawable.icon_clock_arrow_up_24),
                                    contentDescription = StringsKey.SortTimeUpward,
                                    light = true
                                )
                            }

                            SortMode.Downward -> {
                                ContentMenuIcon(
                                    onClick = { PhotoManager.updateSortMode(SortMode.Upward) },
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
                        onClick = { UiController.updateGridMode(false) },
                        painter = painterResource(Res.drawable.icon_grid_on_24),
                        contentDescription = StringsKey.GridOn,
                    )
                } else {
                    ContentMenuIcon(
                        onClick = { UiController.updateGridMode(true) },
                        painter = painterResource(Res.drawable.icon_grid_off_24),
                        contentDescription = StringsKey.GridOff,
                    )
                }
                ContentMenuIcon(
                    onClick = {
                        PhotoManager.refreshCurrentFolder()
                    },
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = StringsKey.Refresh,
                )
            }
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
                if (currentFolder != null) {
                    ContentDensityMenuWidget(
                        currentMode = contentDensityMode,
                    ) {
                        UiController.updateContentDensityMode(it)
                    }
                    ColumnIconMenuButton(
                        imageVector = Icons.Filled.Delete,
                        label = StringsKey.RemoveFolder
                    ) {
                        callClose()
                        currentFolder?.let {
                            PhotoManager.removeFolder(it)
                        }
                    }
                }
                AppSettings()
            }
        }
    ) { contentTop ->
        PhotoItems(
            isGridMode = isGridMode,
            contentTop = contentTop,
            contentDensityMode = contentDensityMode,
            currentFolder = currentFolder,
            isRecycleBin = false,
            photoList = photoList,
            recycleBinDialogState = RecycleBinDialogState.Main
        )
    }
}

