package com.lollipop.photo.state

import androidx.compose.runtime.mutableStateOf

object WindowStateController {

    const val defaultTitle = "PhotoManager"

    val windowTitle = mutableStateOf(defaultTitle)

    val drawerExpand = mutableStateOf(true)

    val showPhotoName = mutableStateOf(true)

    fun updateTitle(title: String) {
        windowTitle.value = title.ifEmpty { defaultTitle }
    }

    fun updateDrawerExpand(expand: Boolean) {
        drawerExpand.value = expand
    }

    fun updateShowPhotoName(show: Boolean) {
        showPhotoName.value = show
    }

}