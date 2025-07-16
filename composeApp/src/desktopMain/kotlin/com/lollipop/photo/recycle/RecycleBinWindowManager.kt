package com.lollipop.photo.recycle

import androidx.compose.runtime.mutableStateListOf
import com.lollipop.photo.data.photo.PhotoRecycleBin

object RecycleBinWindowManager {

    val windows = mutableStateListOf<RecycleBinWindowState>()

    fun open(recycleBin: PhotoRecycleBin) {
        windows += RecycleBinWindowState(
            recycleBin = recycleBin,
            close = { windows.remove(it) }
        )
    }

}