package com.lollipop.photo.data

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.Executors
import javax.swing.JFileChooser

object FileHelper {

    const val FILE_CHOOSER_DIR = "fileChooserDir"

    private const val KEY_FOLLOW_LIST = "followList"
    private const val KEY_DEFAULT_LIST = "defaultList"
    private const val KEY_FOLDER_CONFIG = "folder.config"
    private const val KEY_DIR_PATH = "dirPath"

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

    fun openFileChooser() {

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
            // TODO
//            if (files != null && files.isNotEmpty()) {
//                addAllFile(files)
//                return
//            }
//            val file = chooser.selectedFile
//            if (file != null && file.exists()) {
//                addAllFile(file.path)
//            }
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

    private fun loadFolderInfo(folder: PhotoFolder) {
        val dir = folder.dir
        // TODO
    }

    fun interface PhotoFolderConfigListener {
        fun onLoaded(followList: List<PhotoFolder>, defaultList: List<PhotoFolder>)
    }

}