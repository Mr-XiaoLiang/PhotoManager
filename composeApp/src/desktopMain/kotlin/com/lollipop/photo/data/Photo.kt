package com.lollipop.photo.data

import java.io.File

class Photo(
    val main: PhotoFile,
) {

    val name: String
        get() {
            return main.name
        }
    val preview: String
        get() {
            return main.path
        }

    val compatriot = mutableListOf<PhotoFile>()

}

class PhotoFile(
    val file: File,
) {

    val path: String by lazy {
        file.path
    }

    val name: String by lazy {
        file.name
    }

    val size: Long by lazy {
        file.length()
    }

    val sizeValue: String by lazy {
        getSizeString(size)
    }

    val suffix by lazy {
        PhotoSuffix.findByKey(file.extension)
    }

    val suffixIndex: Int
        get() {
            return suffix?.ordinal ?: -1
        }

    private fun getSizeString(size: Long): String {
        val unit = arrayOf("B", "KB", "MB", "GB", "TB")
        val sizeStep = 1000
        var index = 0
        var fileSize = size.toDouble()
        while (fileSize >= sizeStep) {
            index++
            fileSize /= sizeStep
        }
        return "%.2f %s".format(fileSize, unit[index])
    }

}

class PhotoFolder(
    val dir: File
) {

    val path: String by lazy {
        dir.path
    }

    val name: String by lazy {
        dir.name
    }

    val photoList = mutableListOf<Photo>()
}
