package com.lollipop.pm

import java.util.prefs.Preferences
import kotlin.reflect.KProperty


object PreferencesHelper {

    //返回用户的根首选项节点
    private val preferences: Preferences by lazy {
        Preferences.userRoot()
    }

    val userHome by lazy {
        System.getProperty("user.dir")
    }

    private fun stringValue(def: String) = StringDelegate(def)
    private fun longValue(def: Long) = LongDelegate(def)
    private fun booleanValue(def: Boolean) = BooleanDelegate(def)
    private fun intValue(def: Int) = IntDelegate(def)
    private fun floatValue(def: Float) = FloatDelegate(def)
    private fun doubleValue(def: Double) = DoubleDelegate(def)

    fun getString(key: String, def: String): String {
        return preferences.get(key, def) ?: def
    }

    fun putString(key: String, value: String) {
        preferences.put(key, value)
    }

    fun getLong(key: String, def: Long): Long {
        return preferences.getLong(key, def)
    }

    fun putLong(key: String, value: Long) {
        preferences.putLong(key, value)
    }

    fun getBoolean(key: String, def: Boolean): Boolean {
        return preferences.getBoolean(key, def)
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.putBoolean(key, value)
    }

    fun getInt(key: String, def: Int): Int {
        return preferences.getInt(key, def)
    }

    fun putInt(key: String, value: Int) {
        preferences.putInt(key, value)
    }

    fun getFloat(key: String, def: Float): Float {
        return preferences.getFloat(key, def)
    }

    fun putFloat(key: String, value: Float) {
        preferences.putFloat(key, value)
    }

    fun getDouble(key: String, def: Double): Double {
        return preferences.getDouble(key, def)
    }

    fun putDouble(key: String, value: Double) {
        preferences.putDouble(key, value)
    }

    private class StringDelegate(private val def: String) {
        operator fun getValue(thisRef: PreferencesHelper, property: KProperty<*>): String {
            return thisRef.getString(property.name, def)
        }

        operator fun setValue(thisRef: PreferencesHelper, property: KProperty<*>, value: String) {
            thisRef.putString(property.name, value)
        }
    }

    private class LongDelegate(private val def: Long) {
        operator fun getValue(thisRef: PreferencesHelper, property: KProperty<*>): Long {
            return thisRef.getLong(property.name, def)
        }

        operator fun setValue(thisRef: PreferencesHelper, property: KProperty<*>, value: Long) {
            thisRef.putLong(property.name, value)
        }
    }

    private class BooleanDelegate(private val def: Boolean) {
        operator fun getValue(thisRef: PreferencesHelper, property: KProperty<*>): Boolean {
            return thisRef.getBoolean(property.name, def)
        }

        operator fun setValue(thisRef: PreferencesHelper, property: KProperty<*>, value: Boolean) {
            thisRef.putBoolean(property.name, value)
        }
    }

    private class IntDelegate(private val def: Int) {
        operator fun getValue(thisRef: PreferencesHelper, property: KProperty<*>): Int {
            return thisRef.getInt(property.name, def)
        }

        operator fun setValue(thisRef: PreferencesHelper, property: KProperty<*>, value: Int) {
            thisRef.putInt(property.name, value)
        }
    }

    private class FloatDelegate(private val def: Float) {
        operator fun getValue(thisRef: PreferencesHelper, property: KProperty<*>): Float {
            return thisRef.getFloat(property.name, def)
        }

        operator fun setValue(thisRef: PreferencesHelper, property: KProperty<*>, value: Float) {
            thisRef.putFloat(property.name, value)
        }
    }

    private class DoubleDelegate(private val def: Double) {
        operator fun getValue(thisRef: PreferencesHelper, property: KProperty<*>): Double {
            return thisRef.getDouble(property.name, def)
        }

        operator fun setValue(thisRef: PreferencesHelper, property: KProperty<*>, value: Double) {
            thisRef.putDouble(property.name, value)
        }
    }

}