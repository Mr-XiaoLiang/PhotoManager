package com.lollipop.photo.state

import androidx.compose.runtime.mutableStateOf
import com.lollipop.photo.data.ContentDensityMode

object UiController {

    val gridMode = mutableStateOf(true)
    val contentDensityMode = mutableStateOf(ContentDensityMode.Medium)

}