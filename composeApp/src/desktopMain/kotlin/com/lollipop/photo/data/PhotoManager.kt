package com.lollipop.photo.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.data.photo.PhotoFolder
import com.lollipop.photo.state.WindowStateController
import java.io.File

object PhotoManager {

    val followFolderList = SnapshotStateList<PhotoFolder>()
    val defaultFolderList = SnapshotStateList<PhotoFolder>()
    private val directorySet = mutableSetOf<String>()
    val selectedFolder = mutableStateOf<PhotoFolder?>(null)
    val photoList = SnapshotStateList<Photo>()
    val sortType by lazy { mutableStateOf(Settings.sortType) }
    val sortMode by lazy { mutableStateOf(Settings.sortMode) }

    private var isInit = false

    fun init() {
        if (isInit) {
            return
        }
        isInit = true
        FileHelper.readFolderConfig { followList, defaultList ->
            followFolderList.clear()
            followFolderList.addAll(followList)
            defaultFolderList.clear()
            defaultFolderList.addAll(defaultList)
            sortFolderList()
        }
    }

    fun openFileChooser() {
        FileHelper.openFileChooser { files ->
            for (filePath in files) {
                addNewDirectory(filePath)
            }
            saveDirList()
        }
    }

    private fun saveDirList() {
        FileHelper.saveFolderConfig(followFolderList, defaultFolderList)
    }

    private fun addNewDirectory(filePath: String) {
        if (directorySet.contains(filePath)) {
            return
        }
        val dir = File(filePath)
        directorySet.add(filePath)
        val photoFolder = PhotoFolder(dir)
        FileHelper.loadFolderInfo(photoFolder)
        defaultFolderList.add(photoFolder)
        sortFolderList()
        selectedFolder(photoFolder)
    }

    fun removeFolder(folder: PhotoFolder) {
        followFolderList.remove(folder)
        defaultFolderList.remove(folder)
        if (selectedFolder.value === folder) {
            selectedFolder(followFolderList.firstOrNull() ?: defaultFolderList.firstOrNull())
        }
        saveDirList()
    }

    fun followFolder(folder: PhotoFolder) {
        followFolderList.add(folder)
        defaultFolderList.remove(folder)
        sortFolderList()
        saveDirList()
    }

    fun unfollowFolder(folder: PhotoFolder) {
        defaultFolderList.add(folder)
        followFolderList.remove(folder)
        sortFolderList()
        saveDirList()
    }

    private fun sortFolderList() {
        defaultFolderList.sortBy { it.name }
        followFolderList.sortBy { it.name }
    }

    fun selectedFolder(folder: PhotoFolder?) {
        selectedFolder.value = folder
        photoList.clear()
        doAsync {
            if (folder != null) {
                photoList.addAll(sortPhotoList(folder.photoList))
            }
        }
        WindowStateController.updateTitle(folder?.path ?: "")
    }

    fun refreshCurrentFolder() {
        val folder = selectedFolder.value ?: return
        photoList.clear()
        doAsync {
            folder.load()
            photoList.addAll(sortPhotoList(folder.photos))
        }
    }

    fun updateSortType(sortType: SortType) {
        this.sortType.value = sortType
        Settings.sortType = sortType
        sortCurrentPhotoList()
    }

    fun updateSortMode(sortMode: SortMode) {
        this.sortMode.value = sortMode
        Settings.sortMode = sortMode
        sortCurrentPhotoList()
    }

    private fun sortCurrentPhotoList() {
        doAsync {
            val list = sortPhotoList(photoList.toMutableList())
            photoList.clear()
            photoList.addAll(list)
        }
    }

}