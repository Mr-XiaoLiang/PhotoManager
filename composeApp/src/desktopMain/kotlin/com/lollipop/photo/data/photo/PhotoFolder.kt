package com.lollipop.photo.data.photo

import com.lollipop.photo.data.SortMode
import com.lollipop.photo.data.SortType
import java.io.File

class PhotoFolder(
    dir: File
) : BasicPhotoFolder(dir) {

    val recycleBin = PhotoRecycleBin.create(dir)

    override fun load() {
        super.load()
        recycleBin.load()
    }

    override fun filterDir(dirFile: File): Boolean {
        return !recycleBin.isCurrent(dirFile)
    }

    override fun sort(sortType: SortType, sortMode: SortMode) {
        super.sort(sortType, sortMode)
        recycleBin.sort(sortType, sortMode)
    }

    fun remove(photo: Photo, listener: PhotoRecycleBin.PhotoMoveListener, onEnd: () -> Unit) {
        recycleBin.put(
            photo,
            listener,
            onEnd = {
                onEnd()
                refresh()
            }
        )
    }

    fun restore(photo: Photo, listener: PhotoRecycleBin.PhotoMoveListener, onEnd: () -> Unit) {
        recycleBin.takeOut(
            photo,
            listener,
            onEnd = {
                onEnd()
                refresh()
            }
        )
    }

}