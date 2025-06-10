package com.lollipop.pm.data

import java.io.File

class FileInfo(val file: File) {

    val name by lazy {
        getFileName(file)
    }

    val path by lazy {
        file.path
    }

    val suffix by lazy {
        getFileSuffix(file).lowercase()
    }

    val childCount by lazy {
        if (file.isDirectory) {
            file.list()?.size ?: 0
        } else {
            0
        }
    }

    private fun getFileName(file: File): String {
        val name = file.name
        val index = name.lastIndexOf(".")
        if (index == -1) {
            return name
        }
        return name.substring(0, index)
    }

    private fun getFileSuffix(file: File): String {
        val name = file.name
        val index = name.lastIndexOf(".")
        if (index == -1) {
            return ""
        }
        return name.substring(index + 1)
    }

}