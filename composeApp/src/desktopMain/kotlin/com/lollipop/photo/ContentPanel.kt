package com.lollipop.photo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderDelete
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
import coil3.compose.SubcomposeAsyncImage
import com.lollipop.photo.data.ContentDensityMode
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.data.SortMode
import com.lollipop.photo.data.SortType
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.data.photo.PhotoFolder
import com.lollipop.photo.state.UiController
import com.lollipop.photo.state.WindowStateController
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.values.rememberLanguage
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
        if (isGridMode) {
            GridPhotoPanel(currentFolder, photoList, contentTop, contentDensityMode)
        } else {
            ListPhotoPanel(currentFolder, photoList, contentTop, contentDensityMode)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListPhotoPanel(
    currentFolder: PhotoFolder?,
    photoList: List<Photo>,
    topInsets: Dp,
    densityMode: ContentDensityMode
) {
    val lines = when (densityMode) {
        ContentDensityMode.Less3 -> 1
        ContentDensityMode.Less2 -> 2
        ContentDensityMode.Less1 -> 3
        else -> Int.MAX_VALUE
    }
    LazyColumn {
        item(
            key = "TopSpace",
        ) {
            Spacer(modifier = Modifier.height(topInsets))
        }
        itemsIndexed(items = photoList, key = { _, photo -> photo.preview }) { index, photo ->
            ColumnItemBox(
                densityMode = densityMode,
                index = index,
                onClick = {
                    UiController.openPhotoDetail(photo)
                },
                photo = {
                    PhotoImage(
                        photo = photo,
                        modifier = Modifier.fillMaxSize(),
                    )
                },
                content = {
                    Text(
                        text = photo.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp
                    )
                    if (lines > 2) {
                        Text(
                            text = photo.compatriotNames,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 14.sp
                        )
                    }
                    if (lines > 1) {
                        Text(
                            text = photo.sizeDisplay,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            )
        }
        if (currentFolder != null) {
            item {
                val itemName by rememberLanguage(StringsKey.RecycleBin)
                ColumnItemBox(
                    densityMode = densityMode,
                    index = photoList.size,
                    onClick = {
                        UiController.openPhotoTrash()
                    },
                    photo = {
                        Icon(
                            imageVector = Icons.Filled.FolderDelete,
                            modifier = Modifier.fillMaxWidth(0.6F).aspectRatio(1F),
                            contentDescription = itemName,
                            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.7F)
                        )
                    },
                    content = {
                        Text(
                            text = itemName,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GridPhotoPanel(
    currentFolder: PhotoFolder?,
    photoList: List<Photo>,
    topInsets: Dp,
    densityMode: ContentDensityMode
) {
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
        modifier = Modifier.fillMaxSize().padding(horizontal = 2.dp),
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
            GridItemBox(
                onClick = {
                    UiController.openPhotoDetail(photo)
                }
            ) {
                PhotoImage(
                    photo = photo,
                    modifier = Modifier.fillMaxSize(),
                )
                Text(
                    text = photo.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
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
        if (currentFolder != null) {
            item {
                GridItemBox(
                    backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.07F),
                    onClick = {
                        UiController.openPhotoTrash()
                    }
                ) {
                    val itemName by rememberLanguage(StringsKey.RecycleBin)
                    Icon(
                        imageVector = Icons.Filled.FolderDelete,
                        modifier = Modifier.fillMaxWidth(0.6F).aspectRatio(1F),
                        contentDescription = itemName,
                        tint = MaterialTheme.colors.onBackground.copy(alpha = 0.7F)
                    )
                    Text(
                        text = itemName,
                        modifier = Modifier.fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(color = Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnItemBox(
    densityMode: ContentDensityMode,
    index: Int,
    onClick: () -> Unit,
    photo: @Composable BoxScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    val previewSize = when (densityMode) {
        ContentDensityMode.Less3 -> 32.dp
        ContentDensityMode.Less2 -> 64.dp
        ContentDensityMode.Less1 -> 128.dp
        ContentDensityMode.Medium -> 256.dp
        ContentDensityMode.More1 -> 380.dp
        ContentDensityMode.More2 -> 420.dp
        ContentDensityMode.More3 -> 480.dp
    }
    val backgroundA = MaterialTheme.colors.surface.copy(alpha = 0.05F)
    val backgroundB = MaterialTheme.colors.primary.copy(alpha = 0.05F)
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(
                if (index % 2 == 0) {
                    backgroundA
                } else {
                    backgroundB
                }
            ).onClick(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(previewSize)
                .height(previewSize)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            photo()
        }
        Column(
            modifier = Modifier.weight(1F)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GridItemBox(
    backgroundColor: Color? = null,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val ratio = 0.7F
    val backgroundColor = backgroundColor ?: MaterialTheme.colors.background
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(all = 2.dp)
            .aspectRatio(ratio)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .onClick(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun PhotoImage(
    photo: Photo,
    modifier: Modifier
) {
    SubcomposeAsyncImage(
        model = photo.preview,
        contentDescription = photo.name,
        modifier = modifier,
        loading = {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                )
            }
        },
        contentScale = ContentScale.Crop
    )
}

