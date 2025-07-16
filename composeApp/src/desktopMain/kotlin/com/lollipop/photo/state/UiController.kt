package com.lollipop.photo.state

import androidx.compose.runtime.mutableStateOf
import com.lollipop.photo.RecycleBinDialogState
import com.lollipop.photo.data.ContentDensityMode
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.data.Settings
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.data.photo.PhotoFolder
import com.lollipop.photo.detail.PhotoDetailWindowManager
import com.lollipop.photo.recycle.RecycleBinWindowManager
import java.awt.Desktop
import java.io.File

object UiController {

    val gridMode by lazy {
        mutableStateOf(Settings.gridMode)
    }
    val contentDensityMode by lazy {
        mutableStateOf(Settings.contentDensityMode)
    }

    fun updateGridMode(mode: Boolean) {
        gridMode.value = mode
        Settings.gridMode = mode
    }

    fun updateContentDensityMode(mode: ContentDensityMode) {
        contentDensityMode.value = mode
        Settings.contentDensityMode = mode
    }

    fun openPhotoDetail(photo: Photo) {
        PhotoDetailWindowManager.open(photo)
    }

    fun openPhotoTrash(folder: PhotoFolder) {
        RecycleBinWindowManager.open(folder.recycleBin)
    }

    fun removePhoto(photo: Photo, state: RecycleBinDialogState) {
        state.alertRemovePhoto(photo)
    }

    fun restorePhoto(photo: Photo, state: RecycleBinDialogState) {
        state.alertRestorePhoto(photo)
    }

    fun updateRecycleBin(folder: PhotoFolder) {
        RecycleBinWindowManager.windows.forEach { state ->
            // 刷新回收站窗口
            if (state.recycleBin.parentFolder.path == folder.path) {
                state.refresh()
            }
        }
    }

    fun openCurrentPhotoTrash() {
        PhotoManager.selectedFolder.value?.let {
            openPhotoTrash(it)
        }
    }

    fun openPath(file: File) {
        val desktop = Desktop.getDesktop()
        if (desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(file)
        }
    }

}