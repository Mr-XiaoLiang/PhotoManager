package com.lollipop.photo.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.File

object PhotoManager {

    val followFolderList = SnapshotStateList<PhotoFolder>()
    val defaultFolderList = SnapshotStateList<PhotoFolder>()
    private val directorySet = mutableSetOf<String>()
    val selectedFolder = mutableStateOf<PhotoFolder?>(null)
    val photoList = SnapshotStateList<Photo>()

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
        selectedFolder(photoFolder)
    }

    fun removeFolder(folder: PhotoFolder) {
        followFolderList.remove(folder)
        defaultFolderList.remove(folder)
        if (selectedFolder.value === folder) {
            selectedFolder(followFolderList.firstOrNull() ?: defaultFolderList.firstOrNull())
        }
    }

    fun followFolder(folder: PhotoFolder) {
        followFolderList.add(folder)
        defaultFolderList.remove(folder)
    }

    fun unfollowFolder(folder: PhotoFolder) {
        defaultFolderList.add(folder)
        followFolderList.remove(folder)
    }

    fun selectedFolder(folder: PhotoFolder?) {
        selectedFolder.value = folder
        photoList.clear()
        folder?.let {
            photoList.addAll(it.photoList)
        }
    }

}