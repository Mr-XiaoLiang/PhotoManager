package com.lollipop.photo.detail

import com.lollipop.photo.data.Photo

class PhotoDetailWindowState(
    val photo: Photo,
    private val close: (PhotoDetailWindowState) -> Unit
) {

    val title: String by lazy {
        photo.preview
    }

    fun close() = close(this)
}
