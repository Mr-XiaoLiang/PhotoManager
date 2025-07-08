package com.lollipop.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lollipop.photo.data.*
import com.lollipop.photo.state.UiController
import com.lollipop.photo.state.WindowStateController
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
            if (currentFolder != null) {
                when (sortType) {
                    SortType.Name -> {
                        when (sortMode) {
                            SortMode.Upward -> {
                                ContentMenuIcon(
                                    onClick = { PhotoManager.updateSortMode(SortMode.Downward) },
                                    painter = painterResource(Res.drawable.icon_text_up_24),
                                    contentDescription = "TextUpward",
                                    light = true
                                )
                            }

                            SortMode.Downward -> {
                                ContentMenuIcon(
                                    onClick = { PhotoManager.updateSortMode(SortMode.Upward) },
                                    painter = painterResource(Res.drawable.icon_text_down_24),
                                    contentDescription = "TextDownward",
                                    light = true
                                )
                            }
                        }
                        ContentMenuIcon(
                            onClick = { PhotoManager.updateSortType(SortType.Time) },
                            painter = painterResource(Res.drawable.icon_clock_24),
                            contentDescription = "TimeSort",
                        )
                    }

                    SortType.Time -> {
                        ContentMenuIcon(
                            onClick = { PhotoManager.updateSortType(SortType.Name) },
                            painter = painterResource(Res.drawable.icon_text_24),
                            contentDescription = "TextSort",
                        )
                        when (sortMode) {
                            SortMode.Upward -> {
                                ContentMenuIcon(
                                    onClick = { PhotoManager.updateSortMode(SortMode.Downward) },
                                    painter = painterResource(Res.drawable.icon_clock_arrow_up_24),
                                    contentDescription = "TimeUpward",
                                    light = true
                                )
                            }

                            SortMode.Downward -> {
                                ContentMenuIcon(
                                    onClick = { PhotoManager.updateSortMode(SortMode.Upward) },
                                    painter = painterResource(Res.drawable.icon_clock_arrow_down_24),
                                    contentDescription = "TimeDownward",
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
                        contentDescription = "GridOn",
                    )
                } else {
                    ContentMenuIcon(
                        onClick = { UiController.updateGridMode(true) },
                        painter = painterResource(Res.drawable.icon_grid_off_24),
                        contentDescription = "GridOff",
                    )
                }
                ContentMenuIcon(
                    onClick = {
                        PhotoManager.refreshCurrentFolder()
                    },
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh",
                )
                // 展开菜单的按钮
                ContentMenuIcon(
                    onClick = { callExpand() },
                    painter = painterResource(Res.drawable.window_action_menu_24),
                    contentDescription = "Close",
                )
            }
        },
        menuPanel = { callClose ->
            ColumnMenu(
                modifier = Modifier.width(240.dp),
            ) {
                if (currentFolder != null) {
                    ContentDensityMenuWidget(
                        currentMode = contentDensityMode,
                    ) {
                        UiController.updateContentDensityMode(it)
                    }
                    ColumnIconMenuButton(
                        imageVector = Icons.Filled.Delete,
                        label = "移除文件夹记录"
                    ) {
                        callClose()
                        currentFolder?.let {
                            PhotoManager.removeFolder(it)
                        }
                    }
                }
                ColumnMenuButton(
                    label = "关闭菜单"
                ) {
                    callClose()
                }
            }
        }
    ) {
        if (isGridMode) {
            GridPhotoPanel(photoList, topInsets, contentDensityMode)
        } else {
            ListPhotoPanel(photoList, topInsets, contentDensityMode)
        }
    }
}

@Composable
private fun ListPhotoPanel(photoList: List<Photo>, topInsets: Dp, densityMode: ContentDensityMode) {
    // TODO
}

@Composable
private fun GridPhotoPanel(photoList: List<Photo>, topInsets: Dp, densityMode: ContentDensityMode) {
    val gridMinWidth = when (densityMode) {
        ContentDensityMode.Less3 -> 128.dp
        ContentDensityMode.Less2 -> 198.dp
        ContentDensityMode.Less1 -> 256.dp
        ContentDensityMode.Medium -> 300.dp
        ContentDensityMode.More1 -> 362.dp
        ContentDensityMode.More2 -> 420.dp
        ContentDensityMode.More3 -> 512.dp
    }
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Adaptive(minSize = gridMinWidth)
    ) {
        item(
            key = "TopSpace",
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            Spacer(modifier = Modifier.height(topInsets))
        }
        items(items = photoList, key = { photo -> photo.preview }) { photo ->
            val ratio = 0.7F
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(all = 2.dp)
                    .aspectRatio(ratio)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.BottomCenter
            ) {

                AsyncImage(
                    model = photo.preview,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = photo.name,
                    modifier = Modifier.fillMaxWidth()
                        .background(color = Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = photo.groupCount.toString(),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
                        .align(Alignment.BottomEnd)
                        .clip(shape = RoundedCornerShape(topStart = 8.dp))
                        .background(color = Color.Black.copy(alpha = 0.8f))
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}
