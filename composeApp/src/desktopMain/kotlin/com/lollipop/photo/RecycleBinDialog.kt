package com.lollipop.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.data.photo.PhotoFolder
import com.lollipop.photo.data.photo.PhotoRecycleBin
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.values.rememberLanguage
import java.io.File

object RecycleBinDialog {

    private val moveState = mutableStateOf(false)
    private val moveIndex = mutableIntStateOf(-1)
    private val moveCount = mutableIntStateOf(-1)
    private val moveFrom = mutableStateOf("")
    private val moveTo = mutableStateOf("")
    private val pendingRemovePhoto = mutableStateOf<Photo?>(null)
    private val pendingRestorePhoto = mutableStateOf<Photo?>(null)

    private val onMoveEnd: () -> Unit = {
        moveState.value = false
    }

    private val recycleBinListener = object : PhotoRecycleBin.PhotoMoveListener {
        override fun onMove(
            fromFile: File,
            toFile: File,
            index: Int,
            state: PhotoRecycleBin.PhotoMoveState
        ) {
            if (state == PhotoRecycleBin.PhotoMoveState.MOVING) {
                moveIndex.value = index
                moveFrom.value = fromFile.path
                moveTo.value = toFile.path
            }
        }
    }

    private fun removePhoto(photo: Photo) {
        photo.folder?.let { folder ->
            if (folder is PhotoFolder) {
                moveState.value = true
                moveCount.value = photo.compatriot.size + 1
                folder.remove(photo, recycleBinListener, onMoveEnd)
            }
        }
    }

    private fun restorePhoto(photo: Photo) {
        photo.folder?.let { folder ->
            if (folder is PhotoFolder) {
                moveState.value = true
                moveCount.value = photo.compatriot.size + 1
                folder.restore(photo, recycleBinListener, onMoveEnd)
            }
        }
    }

    fun alertRemovePhoto(photo: Photo) {
        pendingRemovePhoto.value = photo
    }

    fun alertRestorePhoto(photo: Photo) {
        pendingRestorePhoto.value = photo
    }

    @Composable
    fun DialogCompose() {
        ProgressDialog()
        AlertDialog()
    }

    @Composable
    private fun ProgressDialog() {
        val index by remember { moveIndex }
        val count by remember { moveCount }
        val fromPath by remember { moveFrom }
        val toPath by remember { moveTo }
        val isMoving by remember { moveState }
        if (isMoving) {
            Dialog(
                onDismissRequest = {},
            ) {
                Box(
                    modifier = Modifier.background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(16.dp)
                    ).defaultMinSize(minWidth = 400.dp, minHeight = 300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().border(
                                width = 1.dp,
                                color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ).padding(8.dp)
                        ) {
                            Text(text = fromPath)
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val color = MaterialTheme.colors.primary.copy(alpha = 0.3f)
                                HorizontalDivider(
                                    modifier = Modifier.weight(1f),
                                    color = color,
                                    thickness = 2.dp
                                )
                                Icon(imageVector = Icons.Filled.ArrowDownward, contentDescription = null, tint = color)
                                HorizontalDivider(
                                    modifier = Modifier.weight(1f),
                                    color = color,
                                    thickness = 2.dp
                                )
                            }
                            Text(text = toPath)
                        }

                    }

                    LinearProgressIndicator(
                        progress = { index * 1F / count },
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }

    @Composable
    private fun AlertDialog() {
        var removePhoto by remember { pendingRemovePhoto }
        var restorePhoto by remember { pendingRestorePhoto }
        if (removePhoto != null) {
            removePhoto?.let { photo ->
                val hintMessage by rememberLanguage(StringsKey.HintRemoveToRecycleBin)
                AlertPhotoDialog(
                    title = hintMessage,
                    photo = photo,
                    onDismiss = {
                        removePhoto = null
                    },
                    onConfirm = {
                        removePhoto(photo)
                        removePhoto = null
                    }
                )
            }
        } else if (restorePhoto != null) {
            restorePhoto?.let { photo ->
                val hintMessage by rememberLanguage(StringsKey.HintRestorePhotoFromRecycleBin)
                AlertPhotoDialog(
                    title = hintMessage,
                    photo = photo,
                    onDismiss = {
                        restorePhoto = null
                    },
                    onConfirm = {
                        restorePhoto(photo)
                        restorePhoto = null
                    }
                )
            }
        }
    }

    @Composable
    private fun AlertPhotoDialog(
        title: String,
        photo: Photo,
        onDismiss: () -> Unit,
        onConfirm: () -> Unit
    ) {
        val cancelLabel by rememberLanguage(StringsKey.ButtonCancel)
        val confirmLabel by rememberLanguage(StringsKey.ButtonConfirm)
        AlertDialog(
            title = {
                Text(text = title)
            },
            text = {
                Text(
                    text = photo.let { "${it.name}, ${it.compatriotNames}" }
                )
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text(text = confirmLabel)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(cancelLabel)
                }
            }
        )
    }

}

