package com.lollipop.photo.state

import androidx.compose.runtime.mutableStateOf
import com.lollipop.photo.data.ContentDensityMode
import com.lollipop.photo.data.Settings

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

}