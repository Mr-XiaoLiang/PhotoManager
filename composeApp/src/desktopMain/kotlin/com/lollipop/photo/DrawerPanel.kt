package com.lollipop.photo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.data.photo.PhotoFolder
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.values.rememberLanguage
import com.lollipop.photo.widget.ContentBox
import com.lollipop.photo.widget.ContentMenuIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawerPanel(
    modifier: Modifier,
    topInsets: Dp
) {
    val followList = remember { PhotoManager.followFolderList }
    val defaultList = remember { PhotoManager.defaultFolderList }
    val selectedFolder by remember { PhotoManager.selectedFolder }
    ContentBox(
        topInsets = topInsets,
        modifier = modifier,
        menuBar = {
            ContentMenuIcon(
                imageVector = Icons.Filled.Add,
                contentDescription = StringsKey.AddFolder,
                onClick = {
                    PhotoManager.openFileChooser()
                }
            )
        },
        menuPanel = null
    ) { contentTop ->
        if (followList.isEmpty() && defaultList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val label by rememberLanguage(StringsKey.AddFolder)
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = label,
                    modifier = Modifier.width(64.dp)
                        .height(64.dp)
                        .onClick {
                            PhotoManager.openFileChooser()
                        }
                )
                Text(text = label)
            }

        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    items(1) {
                        Spacer(modifier = Modifier.height(contentTop))
                    }

                    items(followList) { folder ->

                        FolderItem(folder, isSelected = selectedFolder?.path == folder.path, isFollow = true)

                    }

                    items(defaultList) { folder ->

                        FolderItem(folder, isSelected = selectedFolder?.path == folder.path, isFollow = false)

                    }

                }
            }
        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FolderItem(folder: PhotoFolder, isSelected: Boolean, isFollow: Boolean) {
    FolderMenuBox(
        folder = folder,
        isFollow = isFollow
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        color = if (isSelected) {
                            MaterialTheme.colors.background
                        } else {
                            Color.Transparent
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .onClick {
                        PhotoManager.selectedFolder(folder)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = folder.name,
                    color = contentColorFor(MaterialTheme.colors.onBackground),
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1F)
                )
                Spacer(modifier = Modifier.width(16.dp))
                ContentMenuIcon(
                    imageVector = if (isFollow) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = if (isFollow) {
                        StringsKey.Follow
                    } else {
                        StringsKey.Unfollow
                    },
                    light = isFollow,
                    onClick = {
                        if (isFollow) {
                            PhotoManager.unfollowFolder(folder)
                        } else {
                            PhotoManager.followFolder(folder)
                        }
                    }
                )
            }
        }
    }
}
