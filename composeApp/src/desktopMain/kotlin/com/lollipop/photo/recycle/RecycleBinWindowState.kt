package com.lollipop.photo.recycle

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.lollipop.photo.RecycleBinDialogState
import com.lollipop.photo.data.PhotoManager
import com.lollipop.photo.data.SortMode
import com.lollipop.photo.data.SortType
import com.lollipop.photo.data.doAsync
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.data.photo.PhotoRecycleBin
import com.lollipop.photo.state.UiController

class RecycleBinWindowState(
    val recycleBin: PhotoRecycleBin,
    private val close: (RecycleBinWindowState) -> Unit
) {

    val title: String by lazy {
        recycleBin.path
    }

    val photoList = SnapshotStateList<Photo>()

    val sortType = mutableStateOf(PhotoManager.sortType.value)
    val sortMode = mutableStateOf(PhotoManager.sortMode.value)
    val gridMode = mutableStateOf(UiController.gridMode.value)
    val contentDensityMode = mutableStateOf(UiController.contentDensityMode.value)

    val recycleBinDialogState = RecycleBinDialogState {
        PhotoManager.refreshFolder(it)
        refresh()
    }

    init {
        refresh()
    }

    fun refresh() {
        doAsync {
            recycleBin.load()
            recycleBin.sort(sortType.value, sortMode.value)
            photoList.clear()
            photoList.addAll(recycleBin.photos)
        }
    }

    fun updateGridMode(mode: Boolean) {
        gridMode.value = mode
    }

    private fun sortCurrentPhotoList() {
        doAsync {
            recycleBin.sort(sortType.value, sortMode.value)
            photoList.clear()
            photoList.addAll(recycleBin.photos)
        }
    }

    fun updateSortType(sortType: SortType) {
        this.sortType.value = sortType
        sortCurrentPhotoList()
    }

    fun updateSortMode(sortMode: SortMode) {
        this.sortMode.value = sortMode
        sortCurrentPhotoList()
    }

    fun close() = close(this)
}