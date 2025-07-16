package com.lollipop.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.size.Size
import com.github.panpf.zoomimage.CoilZoomAsyncImage
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.detail.PhotoDetailWindowState


@Composable
fun PhotoDetailPage(state: PhotoDetailWindowState) {

    AppWindow(
        callClose = state::close,
        title = state.title
    ) { actionBarHeight ->
        PhotoDetail(actionBarHeight = actionBarHeight, photo = state.photo)
    }

}

@Composable
private fun PhotoDetail(actionBarHeight: Dp, photo: Photo) {
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CoilZoomAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(photo.preview)
                .size(Size.ORIGINAL)
                .build(),
            contentDescription = photo.name,
            onLoading = {
                isLoading = true
            },
            onSuccess = {
                isLoading = false
            },
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
            )
        }
    }

}


