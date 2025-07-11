package com.lollipop.photo.values

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberLanguage(key: StringsKey) = remember { Strings.languageOf(key) }

object Strings {

    val defaultLanguage = StringsCN
    val currentLanguage = mutableStateOf<Language?>(null)

    private var isInit = false

    val languageArray = arrayOf(
        StringsCN,
        StringsEN
    )

    fun init() {
        if (isInit) {
            return
        }
        isInit = true
        saveLanguageValues(defaultLanguage)
    }

    fun languageOf(name: StringsKey): MutableState<String> {
        return LanguageValues[name]
    }

    fun setLanguage(language: Language) {
        currentLanguage.value = language
        saveLanguageValues(language)
    }

    private fun saveLanguageValues(language: Language) {
        language.values().forEach { entry ->
            LanguageValues[entry.key] = entry.value
        }
    }

    operator fun get(key: StringsKey): String {
        return currentLanguage.value?.opt(key) ?: defaultLanguage.opt(key) ?: ""
    }

    private object LanguageValues {

        private val currentLanguageMap = mutableMapOf<StringsKey, MutableState<String>>()

        private val noneValue = StableState("")

        operator fun get(key: StringsKey): MutableState<String> {
            if (key == StringsKey.NONE) {
                return noneValue
            }
            val state = currentLanguageMap[key]
            if (state == null) {
                val state = mutableStateOf("")
                currentLanguageMap[key] = state
                return state
            }
            return state
        }

        operator fun set(key: StringsKey, value: String) {
            if (key == StringsKey.NONE) {
                return
            }
            get(key).value = value
        }

    }

    private class StableState<T>(private val valueImpl: T) : MutableState<T> {
        override var value: T
            get() {
                return valueImpl
            }
            set(value) {}

        override operator fun component1(): T = value

        override operator fun component2(): (T) -> Unit = { }
    }

    open class Language(val name: String) {

        protected val stringsMap = mutableMapOf<StringsKey, String>()

        fun values(): List<MutableMap.MutableEntry<StringsKey, String>> {
            return stringsMap.entries.toList()
        }

        fun opt(key: StringsKey): String? {
            return stringsMap[key]
        }

        protected infix fun StringsKey.to(value: String) {
            stringsMap[this] = value
        }

    }

}