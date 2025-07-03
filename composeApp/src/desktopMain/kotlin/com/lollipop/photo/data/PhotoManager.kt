package com.lollipop.photo.data

import androidx.compose.runtime.snapshots.SnapshotStateList

object PhotoManager {

    val followFolderList = SnapshotStateList<PhotoFolder>()
    val defaultFolderList = SnapshotStateList<PhotoFolder>()
    val photoList = SnapshotStateList<Photo>()

}