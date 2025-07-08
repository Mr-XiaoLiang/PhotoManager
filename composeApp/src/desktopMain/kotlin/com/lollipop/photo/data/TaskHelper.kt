package com.lollipop.photo.data

import java.util.concurrent.Executors

object TaskHelper {

    private val executor by lazy {
        Executors.newCachedThreadPool()
    }

    fun post(runnable: Runnable) {
        executor.execute(runnable)
    }

}

inline fun <reified T> T.doAsync(
    crossinline error: (Throwable) -> Unit = { it.printStackTrace() },
    crossinline block: T.() -> Unit
) {
    TaskHelper.post {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }
}
