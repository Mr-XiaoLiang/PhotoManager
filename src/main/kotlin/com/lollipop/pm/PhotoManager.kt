package com.lollipop.pm

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.lollipop.pm.data.CrumbsInfo
import com.lollipop.pm.data.FileSnapshot
import com.lollipop.pm.data.Page
import com.lollipop.pm.data.PhotoInfo

/**
 * 中心的数据管理器，用于管理状态
 */
object PhotoManager {

    /**
     * 分页列表
     */
    val pageList = SnapshotStateList<Page>()

    /**
     * 当前页面
     */
    var currentPage = mutableStateOf<Page?>(null)

    /**
     * 面包屑路径
     */
    val crumbsList = SnapshotStateList<CrumbsInfo>()

    /**
     * 当前文件列表
     */
    val currentFileList = SnapshotStateList<PhotoInfo>()

    /**
     * 收藏的文件夹
     */
    val favoritesList = SnapshotStateList<PhotoInfo.Folder>()

    /**
     * 添加一个文件夹解析
     */
    fun newPage(path: String) {
        val page = Page.create(path)
        pageList.add(page)
        selectPage(page)
    }

    fun selectPage(page: Page) {
        val oldPage = currentPage.value
        // 把老的页面信息存起来
        oldPage?.let {
            // 清理上次记录的面包屑
            it.crumbsList.clear()
            // 记录当前的面包屑信息
            it.crumbsList.addAll(crumbsList)
        }
        // 更新当前页面，触发UI更新
        currentPage.value = page
        // 清理当前的面包屑记录, 恢复面包屑记录
        updateCrumbsList(page)
        // 清空文件列表, 恢复最后的文件列表
        updateFileList(page.lastFileSnapshot())
    }

    fun refreshFileSnapshot() {
        val page = currentPage.value ?: return
        val crumbs = page.lastCrumbs() ?: return
        val fileSnapshot = page.refreshFileSnapshot(crumbs)
        updateFileList(fileSnapshot)
    }

    private fun updateFileList(fileSnapshot: FileSnapshot) {
        currentFileList.clear()
        currentFileList.addAll(fileSnapshot.fileList)
    }

    private fun updateCrumbsList(page: Page) {
        crumbsList.clear()
        crumbsList.addAll(page.crumbsList)
    }

}