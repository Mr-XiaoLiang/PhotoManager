package com.lollipop.photo.detail

import androidx.compose.runtime.mutableStateListOf
import com.lollipop.photo.data.Photo

object PhotoDetailWindowManager {

    val windows = mutableStateListOf<PhotoDetailWindowState>()

    fun open(photo: Photo) {
        windows += PhotoDetailWindowState(
            photo = photo,
            close = { windows.remove(it) }
        )
    }
}