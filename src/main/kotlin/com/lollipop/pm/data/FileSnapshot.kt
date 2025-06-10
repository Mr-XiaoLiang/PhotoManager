package com.lollipop.pm.data

class FileSnapshot(
    val crumbsInfo: CrumbsInfo,
    val fileList: List<PhotoInfo>
) {

    companion object {
        val Empty = FileSnapshot(CrumbsInfo("", ""), emptyList())
    }

}