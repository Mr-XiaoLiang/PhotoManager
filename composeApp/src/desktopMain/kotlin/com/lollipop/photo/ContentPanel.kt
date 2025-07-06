package com.lollipop.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lollipop.photo.data.Photo
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.state.WindowStateController
import com.lollipop.photo.widget.ColumnMenu
import com.lollipop.photo.widget.ColumnMenuButton
import com.lollipop.photo.widget.ContentBox
import com.lollipop.photo.widget.ContentMenuIcon
import org.jetbrains.compose.resources.painterResource
import photomanager.composeapp.generated.resources.Res
import photomanager.composeapp.generated.resources.icon_left_panel_close_24
import photomanager.composeapp.generated.resources.icon_left_panel_open_24
import photomanager.composeapp.generated.resources.window_action_menu_24

@Composable
fun ContentPanel(
    modifier: Modifier,
    topInsets: Dp
) {
    val isDrawerExpand by remember { WindowStateController.drawerExpand }
    val photoList = remember { PhotoManager.photoList }
    val currentFolder by remember { PhotoManager.selectedFolder }
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
                    ColumnMenuButton(
                        label = "移除文件夹记录"
                    ) {
                        callClose()
                        currentFolder?.let {
                            PhotoManager.removeFolder(it)
                        }
                    }
                }
            }
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier,
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            item(
                span = {
                    // LazyGridItemSpanScope:
                    // maxLineSpan
                    GridItemSpan(maxLineSpan)
                }
            ) {
                Spacer(modifier = Modifier.height(topInsets))
            }
            items(photoList) { photo ->
                PhotoItemGrid(photo)
            }
        }
    }
}

@Composable
fun PhotoItemGrid(photo: Photo) {
    val showPhotoName by remember { WindowStateController.showPhotoName }
    val ratio = 0.7F
    Box(
        modifier = Modifier.fillMaxWidth().aspectRatio(ratio).background(MaterialTheme.colors.background),
        contentAlignment = Alignment.BottomCenter
    ) {

        AsyncImage(
            model = photo.preview,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (showPhotoName) {
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
        }

    }
}
