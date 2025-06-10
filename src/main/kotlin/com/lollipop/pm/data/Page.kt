package com.lollipop.pm.data

import java.io.File

class Page private constructor(
    private val rootFile: FileInfo
) {

    companion object {
        private var maxPageId = 0
        private fun getPageId(): Int {
            synchronized(Page::class.java) {
                return maxPageId++
            }
        }

        fun create(path: String): Page {
            val page = Page(FileInfo(File(path)))
            page.crumbsList.add(CrumbsInfo(page.rootFile.name, page.rootFile.path))
            return page
        }

    }

    val id = getPageId()

    /**
     * 面包屑路径
     */
    val crumbsList = mutableListOf<CrumbsInfo>()

    /**
     * 文件列表的集合
     */
    val fileListMap = HashMap<String, FileSnapshot>()

    /**
     * 获取最后的面包屑
     */
    fun lastCrumbs(): CrumbsInfo? {
        return crumbsList.lastOrNull()
    }

    /**
     * 获取最后的面包屑记录
     */
    fun lastFileSnapshot(): FileSnapshot {
        return getFileSnapshot(lastCrumbs() ?: return FileSnapshot.Empty)
    }

    /**
     * 获取文件列表
     */
    fun getFileSnapshot(info: CrumbsInfo): FileSnapshot {
        val path = info.path
        val snapshot = fileListMap[path]
        if (snapshot != null) {
            return snapshot
        }
        val fileSnapshot = buildFileSnapshot(info)
        fileListMap[path] = fileSnapshot
        return fileSnapshot
    }

    /**
     * 刷新文件列表
     */
    fun refreshFileSnapshot(info: CrumbsInfo): FileSnapshot {
        val path = info.path
        fileListMap.remove(path)
        val fileSnapshot = buildFileSnapshot(info)
        fileListMap[path] = fileSnapshot
        return fileSnapshot
    }

    private fun buildFileSnapshot(info: CrumbsInfo): FileSnapshot {
        val file = File(info.path)
        if (!file.exists() || file.isFile) {
            return FileSnapshot(info, emptyList())
        }
        val listFiles = file.listFiles()
        if (listFiles == null || listFiles.isEmpty()) {
            return FileSnapshot(info, emptyList())
        }
        // 通过名字把同一个名字但是不同后缀的文件放在一起
        val fileMap = HashMap<String, FileGroup>()
        for (file in listFiles) {
            val fileInfo = FileInfo(file)
            val fileName = fileInfo.name
            val group = fileMap[fileName] ?: FileGroup()
            group.put(fileInfo)
            fileMap[fileName] = group
        }
        val infoList = fileMap.values.map { it.buildInfo() }
        return FileSnapshot(info, infoList)
    }

    private class FileGroup() {

        private val fileList = ArrayList<FileInfo>()

        fun put(file: FileInfo) {
            fileList.add(file)
        }

        fun buildInfo(): PhotoInfo {
            if (fileList.isEmpty()) {
                return PhotoInfo.Empty
            }
            if (fileList.size == 1) {
                val fileInfo = fileList[0]
                if (fileInfo.file.isDirectory) {
                    return PhotoInfo.Folder(
                        fileInfo.name,
                        fileInfo.path,
                        fileInfo.childCount
                    )
                } else {
                    val suffix = PhotoSuffix.findByKey(fileInfo.suffix)
                    return if (suffix == null) {
                        PhotoInfo.File(fileInfo.name, fileInfo.path, emptyList())
                    } else {
                        PhotoInfo.Photo(fileInfo.name, fileInfo.path, emptyList())
                    }
                }
            }
            val compatriotList = ArrayList<FileInfo>()
            var mainFile: FileInfo = fileList[0]
            for (file in fileList) {
                if (file.path == mainFile.path) {
                    // 忽略自身
                    continue
                }
                val mainSuffix = mainFile.suffix
                val fileSuffix = file.suffix
                when (compare(mainSuffix, fileSuffix)) {
                    Compare.Former -> {
                        compatriotList.add(file)
                    }

                    Compare.Latter -> {
                        compatriotList.add(mainFile)
                        mainFile = file
                    }

                    Compare.Same -> {
                        compatriotList.add(file)
                    }
                }
            }
            return PhotoInfo.Photo(
                name = mainFile.name,
                mainPath = mainFile.path,
                compatriotPath = compatriotList.map { it.path }
            )
        }

        /**
         * 比较两个文件后缀
         */
        private fun compare(suffix1: String, suffix2: String): Compare {
            if (suffix1 == suffix2) {
                return Compare.Same
            }
            val suffixType1 = PhotoSuffix.findByKey(suffix1)
            val suffixType2 = PhotoSuffix.findByKey(suffix2)
            if (suffixType1 == null && suffixType2 == null) {
                return Compare.Same
            }
            if (suffixType1 == null) {
                return Compare.Latter
            }
            if (suffixType2 == null) {
                return Compare.Former
            }
            if (suffixType1 == suffixType2) {
                return Compare.Same
            }
            if (suffixType1.ordinal < suffixType2.ordinal) {
                return Compare.Former
            }
            return Compare.Latter
        }

    }

    private enum class Compare {
        Former,
        Latter,
        Same
    }

}