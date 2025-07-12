package com.lollipop.photo.data.photo

import com.lollipop.photo.data.ConfigHelper
import kotlinx.coroutines.Runnable
import java.io.File
import java.util.concurrent.Executors

/**
 * 回收站，这是一个文件夹，里面存放的是被删除的图片
 * 使用方式是将准备删除的文件放到这个文件夹中，从而避免误操作
 */
class PhotoRecycleBin(
    private val parentFolder: File,
    folder: File
) : BasicPhotoFolder(folder) {

    companion object {
        const val FOLDER_NAME = "RecycleBin"

        fun create(photoFolder: File): PhotoRecycleBin {
            val folder = File(photoFolder, FOLDER_NAME)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            return PhotoRecycleBin(photoFolder, folder)
        }

    }

    private val config = ConfigHelper.jsonConfig(dir, "config.ll")

    private val taskHandler by lazy {
        Executors.newSingleThreadExecutor()
    }

    fun isCurrent(file: File): Boolean {
        return file == dir
    }

    override fun filterDir(dirFile: File): Boolean {
        return true
    }

    fun put(photo: Photo, listener: PhotoMoveListener, onEnd: () -> Unit) {
        taskHandler.execute(PutTask(this, photo, listener, onEnd))
    }

    fun takeOut(photo: Photo, listener: PhotoMoveListener, onEnd: () -> Unit) {
        taskHandler.execute(TakeOutTask(this, photo, listener, onEnd))
    }

    fun interface PhotoMoveListener {

        fun onMove(fromFile: File, toFile: File, index: Int, state: PhotoMoveState)

    }

    enum class PhotoMoveState {

        MOVING,

        MOVED,

        MOVE_FAILED,

    }

    private abstract class BasicTask(
        protected val recycleBin: PhotoRecycleBin,
    ) : Runnable {
        protected inline fun <reified B, reified R, reified F> transaction(
            before: () -> B,
            block: (B) -> R,
            after: (B, R?) -> F,
            error: (Throwable) -> F? = { null }
        ): F? {
            return tryDo<F>(error) {
                // 开始事务会影响中间流程，允许中断
                val b = before()
                // 中间事务不影响最终结果
                val r = tryDo<R> {
                    block(b)
                }
                after(b, r)
            }
        }

        protected val config: ConfigHelper.Json
            get() {
                return recycleBin.config
            }

        protected inline fun <reified T> tryDo(error: (Throwable) -> T? = { null }, block: () -> T): T? {
            try {
                return block()
            } catch (e: Throwable) {
                e.printStackTrace()
                return error(e)
            }
        }

        protected fun fileMove(sourceFile: File, destFile: File, index: Int, listener: PhotoMoveListener): Boolean {
            if (!sourceFile.exists()) {
                return false
            }
            return transaction(
                before = {
                    listener.onMove(sourceFile, destFile, index, PhotoMoveState.MOVING)
                },
                block = {
                    sourceFile.renameTo(destFile)
                },
                after = { _, result ->
                    if (result == true) {
                        listener.onMove(
                            sourceFile,
                            destFile,
                            index,
                            PhotoMoveState.MOVED
                        )
                    } else {
                        listener.onMove(
                            sourceFile,
                            destFile,
                            index,
                            PhotoMoveState.MOVE_FAILED
                        )
                    }
                    result
                }
            ) ?: false
        }

    }

    private class TakeOutTask(
        recycleBin: PhotoRecycleBin,
        private val photo: Photo,
        private val listener: PhotoMoveListener,
        private val onEnd: () -> Unit
    ) : BasicTask(recycleBin) {
        override fun run() {
            fileMoveOutSync(photo, listener)
            onEnd()
        }

        private fun forgetMove(file: File): File {
            val srcPath = file.path
            val targetPath = config.optString(srcPath, "")
            if (targetPath.isNotEmpty()) {
                return File(targetPath)
            }
            return file
        }

        private fun findFileMap(file: File): File {
            val srcPath = config.optString(file.path, "")
            if (srcPath.isNotEmpty()) {
                return File(srcPath)
            }
            return File(recycleBin.parentFolder, file.name)
        }

        private fun fileMoveOutSync(photo: Photo, listener: PhotoMoveListener) {
            transaction(
                before = {
                    // 先同步一下
                    config.load()
                },
                block = {
                    // 移动主文件
                    fileMoveOut(photo.main.file, 0, listener)
                    // 移动其他文件
                    val photoFiles = photo.compatriot
                    for (index in photoFiles.indices) {
                        val file = photoFiles[index]
                        fileMoveOut(file.file, index + 1, listener)
                    }
                },
                after = { _, _ ->
                    // 然后保存
                    config.save()
                }
            )
        }

        private fun fileMoveOut(sourceFile: File, index: Int, listener: PhotoMoveListener) {
            transaction(
                before = {
                    findFileMap(sourceFile)
                },
                block = { destFile ->
                    fileMove(sourceFile, destFile, index, listener)
                },
                after = { destFile, result ->
                    if (result == true) {
                        // 移除移动记录
                        forgetMove(sourceFile)
                    }
                }
            )
        }

    }

    private class PutTask(
        recycleBin: PhotoRecycleBin,
        private val photo: Photo,
        private val listener: PhotoMoveListener,
        private val onEnd: () -> Unit
    ) : BasicTask(recycleBin) {

        override fun run() {
            fileMoveInSync(photo, listener)
            onEnd()
        }

        private fun fileMoveInSync(photo: Photo, listener: PhotoMoveListener) {
            transaction(
                before = {
                    // 先同步一下
                    config.load()
                },
                block = {
                    // 移动主文件
                    fileMoveIn(photo.main.file, 0, listener)
                    // 移动其他文件
                    val photoFiles = photo.compatriot
                    for (index in photoFiles.indices) {
                        val file = photoFiles[index]
                        fileMoveIn(file.file, index + 1, listener)
                    }
                },
                after = { _, _ ->
                    // 然后保存
                    config.save()
                }
            )
        }

        private fun rememberMove(fromFile: File, toFile: File) {
            config.build {
                put(toFile.path, fromFile.path)
            }
        }

        private fun fileMoveIn(sourceFile: File, index: Int, listener: PhotoMoveListener) {
            transaction(
                before = {
                    val fileName = sourceFile.name
                    // 移动到当前的文件夹
                    File(recycleBin.dir, fileName)
                },
                block = { destFile ->
                    fileMove(sourceFile, destFile, index, listener)
                },
                after = { destFile, result ->
                    if (result == true) {
                        // 记录移动记录
                        rememberMove(sourceFile, destFile)
                    }
                }
            )
        }

    }

}