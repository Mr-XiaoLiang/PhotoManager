package com.lollipop.photo.data

import org.json.JSONArray
import org.json.JSONObject
import java.io.File

abstract class ConfigHelper(
    val configFile: File
) {

    companion object {
        fun createFile(dir: File, name: String): File {
            val file = File(dir, name)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return file
        }

        fun jsonConfig(dir: File, name: String): Json {
            return Json(createFile(dir, name))
        }
    }

    abstract fun load()

    abstract fun save()

    protected fun loadConfig(): String {
        try {
            if (!configFile.exists()) {
                return ""
            }
            if (configFile.isDirectory) {
                configFile.delete()
                return ""
            }
            return configFile.readText(Charsets.UTF_8)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return ""
    }

    protected fun saveConfig(config: String) {
        try {
            configFile.writeText(config, Charsets.UTF_8)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    class Json(configFile: File) : ConfigHelper(configFile) {

        private var jsonObject: JSONObject = JSONObject()

        fun optString(key: String, def: String): String {
            return jsonObject.optString(key, def) ?: def
        }

        fun optInt(key: String, def: Int): Int {
            return jsonObject.optInt(key, def) ?: def
        }

        fun optBoolean(key: String, def: Boolean): Boolean {
            return jsonObject.optBoolean(key, def) ?: def
        }

        fun optLong(key: String, def: Long): Long {
            return jsonObject.optLong(key, def) ?: def
        }

        fun optFloat(key: String, def: Float): Float {
            return jsonObject.optFloat(key, def) ?: def
        }

        fun optDouble(key: String, def: Double): Double {
            return jsonObject.optDouble(key, def) ?: def
        }

        fun optObject(key: String, def: () -> JSONObject): JSONObject {
            return jsonObject.optJSONObject(key) ?: def()
        }

        fun optArray(key: String, def: () -> JSONArray): JSONArray {
            return jsonObject.optJSONArray(key) ?: def()
        }

        fun remove(key: String) {
            jsonObject.remove(key)
        }

        fun build(block: JSONObject.() -> Unit) {
            try {
                jsonObject.block()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        override fun load() {
            try {
                val config = loadConfig()
                jsonObject = if (config.isEmpty()) {
                    JSONObject()
                } else {
                    JSONObject(config)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                jsonObject = JSONObject()
            }
        }

        override fun save() {
            try {
                val json = jsonObject.toString()
                saveConfig(json)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

    }

}