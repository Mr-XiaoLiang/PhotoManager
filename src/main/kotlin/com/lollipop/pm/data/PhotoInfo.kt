package com.lollipop.pm.data

sealed class PhotoInfo {

    object Empty : PhotoInfo()

    class Photo(
        val name: String,
        val mainPath: String,
        val compatriotPath: List<String>
    ) : PhotoInfo()

    class File(
        val name: String,
        val path: String,
        val compatriotPath: List<String>
    ) : PhotoInfo()

    class Folder(
        val name: String,
        val path: String,
        val childCount: Int
    ) : PhotoInfo()

}