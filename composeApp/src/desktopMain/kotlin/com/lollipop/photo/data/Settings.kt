package com.lollipop.photo.data

object Settings {

    private const val KEY_SORT_MODE = "sortMode"
    private const val KEY_SORT_TYPE = "sortType"
    private const val KEY_GRID_MODE = "gridMode"
    private const val KEY_CONTENT_DENSITY_MODE = "contentDensityMode"

    var gridMode: Boolean
        get() {
            return PreferencesHelper.opt(KEY_GRID_MODE, true)
        }
        set(value) {
            PreferencesHelper.set(KEY_GRID_MODE, value)
        }

    var contentDensityMode: ContentDensityMode
        get() {
            return PreferencesHelper.opt(KEY_CONTENT_DENSITY_MODE, ContentDensityMode.Medium) { name ->
                ContentDensityMode.entries.find { it.name == name }
            }
        }
        set(value) {
            PreferencesHelper.set(KEY_CONTENT_DENSITY_MODE, value.name)
        }

    var sortType: SortType
        get() {
            return PreferencesHelper.opt(KEY_SORT_TYPE, SortType.Time) { name ->
                SortType.entries.find { name == it.name }
            }
        }
        set(value) {
            PreferencesHelper.set(KEY_SORT_TYPE, value.name)
        }

    var sortMode: SortMode
        get() {
            return PreferencesHelper.opt(KEY_SORT_MODE, SortMode.Upward) { name ->
                SortMode.entries.find { name == it.name }
            }
        }
        set(value) {
            PreferencesHelper.set(KEY_SORT_MODE, value.name)
        }

}