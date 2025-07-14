package com.lollipop.photo.state

import androidx.compose.runtime.mutableStateOf
import com.lollipop.photo.RecycleBinDialog
import com.lollipop.photo.data.ContentDensityMode
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.data.Settings
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.data.photo.PhotoFolder
import com.lollipop.photo.detail.PhotoDetailWindowManager
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
        // TODO
    }

    fun removePhoto(photo: Photo) {
        RecycleBinDialog.alertRemovePhoto(photo)
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