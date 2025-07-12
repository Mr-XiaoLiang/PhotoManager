package com.lollipop.photo.data.photo

import com.lollipop.photo.data.PhotoSuffix
import java.io.File


class PhotoFile(
    val file: File,
) {

    companion object {
        fun getSizeString(size: Long): String {
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
        PhotoSuffix.Companion.findByKey(file.extension)
    }

    val suffixIndex: Int
        get() {
            return suffix?.ordinal ?: -1
        }

}