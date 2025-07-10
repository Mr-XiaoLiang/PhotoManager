package com.lollipop.photo.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp

object WindowConfig {

    val dragAreaHeight = 32.dp
    val actionBarHeight = dragAreaHeight + 12.dp
    val actionBarMargin = 6.dp

    val keepOnBackground = mutableStateOf(false)

    fun updateKeepOnBackground(keepOn: Boolean) {
        keepOnBackground.value = keepOn
    }

}