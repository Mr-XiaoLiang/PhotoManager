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
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.data.photo.PhotoFolder
import com.lollipop.photo.state.UiController
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.values.rememberLanguage

@Composable
fun PhotoItems(
    isGridMode: Boolean,
    contentTop: Dp,
    contentDensityMode: ContentDensityMode,
    currentFolder: PhotoFolder?,
    isRecycleBin: Boolean,
    recycleBinDialogState: RecycleBinDialogState,
    photoList: List<Photo>,
) {
    if (isGridMode) {
        GridPhotoPanel(
            currentFolder = currentFolder,
            isRecycleBin = isRecycleBin,
            photoList = photoList,
            topInsets = contentTop,
            recycleBinDialogState = recycleBinDialogState,
            densityMode = contentDensityMode
        )
    } else {
        ListPhotoPanel(
            currentFolder = currentFolder,
            isRecycleBin = isRecycleBin,
            photoList = photoList,
            topInsets = contentTop,
            recycleBinDialogState = recycleBinDialogState,
            densityMode = contentDensityMode
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListPhotoPanel(
    currentFolder: PhotoFolder?,
    isRecycleBin: Boolean,
    photoList: List<Photo>,
    topInsets: Dp,
    recycleBinDialogState: RecycleBinDialogState,
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
            PhotoMenuBox(
                menuList = if (isRecycleBin) {
                    RecycleBinMenu(photo, recycleBinDialogState)
                } else {
                    PhotoMenu(photo, recycleBinDialogState)
                }
            ) {
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
        }
        if (currentFolder != null) {
            item {
                val itemName by rememberLanguage(StringsKey.RecycleBin)
                ColumnItemBox(
                    densityMode = densityMode,
                    index = photoList.size,
                    onClick = {
                        UiController.openCurrentPhotoTrash()
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
    isRecycleBin: Boolean,
    photoList: List<Photo>,
    topInsets: Dp,
    recycleBinDialogState: RecycleBinDialogState,
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
            PhotoMenuBox(
                menuList = if (isRecycleBin) {
                    RecycleBinMenu(photo, recycleBinDialogState)
                } else {
                    PhotoMenu(photo, recycleBinDialogState)
                }
            ) {
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
        }
        if (currentFolder != null) {
            item {
                GridItemBox(
                    backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.07F),
                    onClick = {
                        UiController.openCurrentPhotoTrash()
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
    // 为什么正式包不能加载图片呢？
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
