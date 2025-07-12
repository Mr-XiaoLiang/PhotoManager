package com.lollipop.photo.data.photo

class Photo(
    val main: PhotoFile,
) {

    val name: String
        get() {
            return main.name
        }
    val preview: String
        get() {
            return main.path
        }

    val compatriot = mutableListOf<PhotoFile>()

    val groupCount: Int by lazy {
        1 + compatriot.size
    }

    val compatriotNames by lazy {
        compatriot.joinToString(separator = ", ", transform = { it.name })
    }

    val sizeDisplay by lazy {
        getAllSizeDisplay()
    }

    private fun getAllSizeDisplay(): String {
        var sizeAll = main.size
        for (compatriot in compatriot) {
            sizeAll += compatriot.size
        }
        val builder = StringBuilder()
        builder.append(PhotoFile.getSizeString(sizeAll))
        builder.append(" = ")
        builder.append(main.sizeValue)
        for (compatriot in compatriot) {
            builder.append(" + ")
            builder.append(compatriot.sizeValue)
        }
        return builder.toString()
    }

}
