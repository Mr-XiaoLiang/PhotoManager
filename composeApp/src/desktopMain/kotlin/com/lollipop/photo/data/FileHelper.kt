package com.lollipop.photo.data

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import javax.swing.JFileChooser

object FileHelper {

    const val FILE_CHOOSER_DIR = "fileChooserDir"

    private const val KEY_FOLLOW_LIST = "followList"
    private const val KEY_DEFAULT_LIST = "defaultList"
    private const val KEY_FOLDER_CONFIG = "folder.config"

    private val executor by lazy {
        Executors.newCachedThreadPool()
    }

    private val userHomeDir by lazy {
        File(System.getProperty("user.home"))
    }

    private val appHomeDir by lazy {
        File(userHomeDir, "LollipopPhoto").also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
    }

    private val folderConfig by lazy {
        File(appHomeDir, KEY_FOLDER_CONFIG)
    }

    private var lastOpenDir: File? = null

    private fun doAsync(block: () -> Unit) {
        executor.execute {
            try {
                block()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun fileChooserDir(): File {
        val last = lastOpenDir
        if (last != null) {
            if (last.exists()) {
                return last
            }
        }
        resumeFileChooserDir()?.let {
            return it
        }
        if (userHomeDir.exists()) {
            return userHomeDir
        }
        return File(".")
    }

    private fun rememberFileChooserDir(dir: File) {
        lastOpenDir = dir
        PreferencesHelper[FILE_CHOOSER_DIR] = dir.path
    }

    private fun resumeFileChooserDir(): File? {
        PreferencesHelper[FILE_CHOOSER_DIR]?.let { path ->
            if (path.isNotEmpty()) {
                val dir = File(path)
                if (dir.exists()) {
                    lastOpenDir = dir
                    return dir
                }
            }
        }
        return null
    }

    fun openFileChooser(callback: (List<String>) -> Unit) {
        val currentDir = fileChooserDir()
        val chooser = JFileChooser()
        chooser.setCurrentDirectory(currentDir)
        chooser.setDialogTitle("Directories")
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)

        // 禁用“所有文件”选项
        chooser.setAcceptAllFileFilterUsed(false)

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            chooser.currentDirectory?.let {
                rememberFileChooserDir(it)
            }
            val files = chooser.selectedFiles?.map { it.path }
            if (files != null && files.isNotEmpty()) {
                callback(files)
                return
            }
            val file = chooser.selectedFile
            if (file != null && file.exists()) {
                callback(listOf(file.path))
            }
        }
    }

    fun writeFile(file: File, content: String) {
        file.writeText(content)
    }

    fun writeJson(file: File, json: JSONObject) {
        writeFile(file, json.toString())
    }

    fun writeJson(file: File, json: JSONArray) {
        writeFile(file, json.toString())
    }

    fun readFile(file: File): String {
        return try {
            file.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun readJsonObject(file: File): JSONObject {
        return try {
            JSONObject(readFile(file))
        } catch (e: Exception) {
            e.printStackTrace()
            JSONObject()
        }
    }

    fun readJsonArray(file: File): JSONArray {
        return try {
            JSONArray(readFile(file))
        } catch (e: Exception) {
            e.printStackTrace()
            JSONArray()
        }
    }

    fun saveFolderConfig(followList: List<PhotoFolder>, defaultList: List<PhotoFolder>) {
        doAsync {
            val jsonObject = JSONObject()
            val followJson = JSONArray()
            for (folder in followList) {
                followJson.put(folder.path)
            }
            jsonObject.put(KEY_FOLLOW_LIST, followJson)
            val defaultJson = JSONArray()
            for (folder in defaultList) {
                defaultJson.put(folder.path)
            }
            jsonObject.put(KEY_DEFAULT_LIST, defaultJson)
            writeJson(folderConfig, jsonObject)
        }
    }

    fun readFolderConfig(callback: PhotoFolderConfigListener) {
        doAsync {
            val jsonObject = readJsonObject(folderConfig)
            val followJson = jsonObject.optJSONArray(KEY_FOLLOW_LIST)
            val followList = mutableListOf<PhotoFolder>()
            if (followJson != null) {
                parseFolder(followJson, followList)
            }
            val defaultJson = jsonObject.optJSONArray(KEY_DEFAULT_LIST)
            val defaultList = mutableListOf<PhotoFolder>()
            if (defaultJson != null) {
                parseFolder(defaultJson, defaultList)
            }
            callback.onLoaded(followList = followList, defaultList = defaultList)
        }
    }

    private fun parseFolder(jsonArray: JSONArray, outList: MutableList<PhotoFolder>) {
        val folderSize = jsonArray.length()
        if (folderSize > 0) {
            for (i in 0 until folderSize) {
                val path = jsonArray.optString(i)
                if (path.isNotEmpty()) {
                    val dir = File(path)
                    if (dir.isDirectory && dir.exists()) {
                        val folder = PhotoFolder(dir)
                        loadFolderInfo(folder)
                        outList.add(folder)
                    }
                }
            }
        }
    }

    fun loadFolderInfo(folder: PhotoFolder) {
        val dir = folder.dir
        // 把文件以名字分组，忽略后缀，但是前提得是符合规则的照片
        val photoMap = mutableMapOf<String, MutableList<PhotoFile>>()
        // 文件队列
        val pendingList = LinkedList<File>()
        pendingList.add(dir)
        // 循环任务队列
        while (pendingList.isNotEmpty()) {
            // 按顺序拿
            val file = pendingList.removeFirst()
            // 如果是文件，就放到队列里
            if (file.isDirectory) {
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
                    if (PhotoSuffix.isPhoto(suffix)) {
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
                for (photo in photoGroup) {
                    if (photo !== mainPhoto) {
                        element.compatriot.add(photo)
                    }
                }
                photoList.add(element)
            }
        }
    }

    private fun getFileSuffix(file: File): String {
        return file.extension
    }

    private fun getFileName(file: File): String {
        return file.nameWithoutExtension
    }

    fun interface PhotoFolderConfigListener {
        fun onLoaded(followList: List<PhotoFolder>, defaultList: List<PhotoFolder>)
    }

}