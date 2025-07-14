package com.lollipop.photo

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import com.lollipop.photo.data.photo.Photo
import com.lollipop.photo.values.StringsKey
import com.lollipop.photo.values.rememberLanguage

object RecycleBinDialog {

    private val moveState = mutableStateOf(false)
    private val moveIndex = mutableIntStateOf(-1)
    private val pendingRemovePhoto = mutableStateOf<Photo?>(null)

    private fun removePhoto(photo: Photo) {
        // TODO
    }

    fun alertRemovePhoto(photo: Photo) {
        pendingRemovePhoto.value = photo
    }

    @Composable
    fun DialogCompose() {
        ProgressDialog()
        AlertDialog()
    }

    @Composable
    private fun ProgressDialog() {
        // TODO
    }

    @Composable
    private fun AlertDialog() {
        var photo by remember { pendingRemovePhoto }
        val hintMessage by rememberLanguage(StringsKey.HintRemoveToRecycleBin)
        val cancelLabel by rememberLanguage(StringsKey.ButtonCancel)
        val confirmLabel by rememberLanguage(StringsKey.ButtonConfirm)
        if (photo != null) {
            AlertDialog(
                title = {
                    Text(text = photo?.name ?: "")
                },
                text = {
                    Text(text = hintMessage)
                },
                onDismissRequest = {
                    photo = null
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            photo?.let {
                                removePhoto(it)
                            }
                            photo = null
                        }
                    ) {
                        Text(text = confirmLabel)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            photo = null
                        }
                    ) {
                        Text(cancelLabel)
                    }
                }
            )
        }
    }

}

