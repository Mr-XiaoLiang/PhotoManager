package com.lollipop.photo.data.photo

import com.lollipop.photo.data.PhotoSuffix
import com.lollipop.photo.data.SortMode
import com.lollipop.photo.data.SortType
import java.io.File
import java.util.*

abstract class BasicPhotoFolder(
    val dir: File
) {

    companion object {
        fun loadFolderInfo(folder: BasicPhotoFolder, folderFilter: (File) -> Boolean) {
            val rootDir = folder.dir
            // 把文件以名字分组，忽略后缀，但是前提得是符合规则的照片
            val photoMap = mutableMapOf<String, MutableList<PhotoFile>>()
            // 文件队列
            val pendingList = LinkedList<File>()
            pendingList.add(rootDir)
            // 循环任务队列
            while (pendingList.isNotEmpty()) {
                // 按顺序拿
                val file = pendingList.removeFirst()
                // 如果是文件，就放到队列里
                if (file.isDirectory) {
                    if (!folderFilter(file)) {
                        continue
                    }
                    val files = file.listFiles()
                    if (files != null && files.isNotEmpty()) {
                        files.forEach {
                            pendingList.add(it)
                        }
                    }
                } else if (file.isFile) {
                    // 是文件，就先检查是不是可用的
                    if (file.exists()) {
                        // 然后看看后缀对不对
                        val suffix = getFileSuffix(file)
                        if (PhotoSuffix.Companion.isPhoto(suffix)) {
                            // 最后根据名字放在一起
                            val fileName = getFileName(file)
                            val photoGroup = photoMap[fileName] ?: mutableListOf()
                            photoGroup.add(PhotoFile(file))
                            photoMap[fileName] = photoGroup
                        }
                    }
                }
            }

            val photoList = folder.photoList
            photoList.clear()
            photoMap.values.forEach { photoGroup ->
                var mainPhoto = photoGroup.firstOrNull()
                for (photo in photoGroup) {
                    if (mainPhoto === photo) {
                        continue
                    }
                    if (mainPhoto == null || mainPhoto.suffixIndex > photo.suffixIndex) {
                        mainPhoto = photo
                    }
                }
                if (mainPhoto != null) {
                    val element = Photo(mainPhoto)
                    element.folder = folder
                    for (photo in photoGroup) {
                        if (photo !== mainPhoto) {
                            element.compatriot.add(photo)
                        }
                    }
                    photoList.add(element)
                }
            }
        }

        fun sortPhotoList(
            list: List<Photo>,
            sortType: SortType,
            sortMode: SortMode
        ): List<Photo> {
            val cacheList = list.toMutableList()
            cacheList.sortWith { o1, o2 ->
                when (sortType) {
                    SortType.Name -> {
                        when (sortMode) {
                            SortMode.Upward -> {
                                o1.name.compareTo(o2.name)
                            }

                            SortMode.Downward -> {
                                o2.name.compareTo(o1.name)
                            }
                        }
                    }

                    SortType.Time -> {
                        when (sortMode) {
                            SortMode.Upward -> {
                                o1.main.file.lastModified().compareTo(o2.main.file.lastModified())
                            }

                            SortMode.Downward -> {
                                o2.main.file.lastModified().compareTo(o1.main.file.lastModified())
                            }
                        }
                    }
                }
            }
            return cacheList
        }

        private fun getFileSuffix(file: File): String {
            return file.extension
        }

        private fun getFileName(file: File): String {
            return file.nameWithoutExtension
        }
    }

    protected var lastSortType: SortType = SortType.Name
    protected var lastSortMode: SortMode = SortMode.Upward

    val path: String by lazy {
        dir.path
    }

    val name: String by lazy {
        dir.name
    }

    protected val photoList = mutableListOf<Photo>()

    val photos: List<Photo>
        get() {
            return photoList
        }

    open fun load() {
        loadFolderInfo(this, ::filterDir)
    }

    protected abstract fun filterDir(dirFile: File): Boolean

    open fun sort(sortType: SortType, sortMode: SortMode) {
        lastSortType = sortType
        lastSortMode = sortMode
        val sortResult = sortPhotoList(photoList, sortType, sortMode)
        photoList.clear()
        photoList.addAll(sortResult)
    }

    fun refresh() {
        load()
        sort(lastSortType, lastSortMode)
    }

}