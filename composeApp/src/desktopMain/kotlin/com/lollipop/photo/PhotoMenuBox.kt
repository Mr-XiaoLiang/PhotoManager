package com.lollipop.photo

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.data.photo.PhotoFolder
import com.lollipop.photo.state.UiController
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.values.rememberLanguage

@Composable
fun PhotoMenuBox(
    photo: Photo,
    content: @Composable () -> Unit
) {
    val openLabel by rememberLanguage(StringsKey.OpenPath)
    val removePhotoLabel by rememberLanguage(StringsKey.RemovePhoto)
    ContextMenuArea(
        items = {
            val itemList = mutableListOf<ContextMenuItem>()
            itemList.add(
                ContextMenuItem(openLabel) {
                    UiController.openPath(photo.main.file)
                }
            )
            itemList.add(
                ContextMenuItem(removePhotoLabel) {
                    UiController.removePhoto(photo)
                }
            )
            photo.compatriot.forEach {
                itemList.add(
                    ContextMenuItem("$openLabel ${it.name}") {
                        UiController.openPath(it.file)
                    }
                )
            }
            itemList
        },
        content = content
    )
}

@Composable
fun FolderMenuBox(
    folder: PhotoFolder,
    isFollow: Boolean,
    content: @Composable () -> Unit
) {
    val removeFolderLabel by rememberLanguage(StringsKey.RemoveFolder)
    val followLabel by rememberLanguage(StringsKey.Follow)
    val unfollowLabel by rememberLanguage(StringsKey.Unfollow)
    val recycleBinLabel by rememberLanguage(StringsKey.RecycleBin)
    val openLabel by rememberLanguage(StringsKey.OpenPath)
    ContextMenuArea(
        items = {
            val itemList = mutableListOf<ContextMenuItem>()
            itemList.add(
                ContextMenuItem(removeFolderLabel) {
                    PhotoManager.removeFolder(folder)
                }
            )
            if (isFollow) {
                itemList.add(
                    ContextMenuItem(unfollowLabel) {
                        PhotoManager.unfollowFolder(folder)
                    }
                )
            } else {
                itemList.add(
                    ContextMenuItem(followLabel) {
                        PhotoManager.followFolder(folder)
                    }
                )
            }
            itemList.add(
                ContextMenuItem(recycleBinLabel) {
                    UiController.openPhotoTrash(folder)
                }
            )
            itemList.add(
                ContextMenuItem(openLabel) {
                    UiController.openPath(folder.dir)
                }
            )
            itemList
        },
        content = content
    )
}
