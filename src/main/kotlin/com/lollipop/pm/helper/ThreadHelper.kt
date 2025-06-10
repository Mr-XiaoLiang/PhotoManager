package com.lollipop.pm.helper

import java.util.concurrent.Executors

object ThreadHelper {

    private val ioExecutor by lazy {
        Executors.newSingleThreadExecutor()
    }

    fun post(block: () -> Unit) {
        ioExecutor.execute(SafeRunnable(block))
    }

    private class SafeRunnable(private val block: () -> Unit) : Runnable {
        override fun run() {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

inline fun <reified T> T.doAsync(crossinline block: T.() -> Unit) {
    ThreadHelper.post {
        block()
    }
}
