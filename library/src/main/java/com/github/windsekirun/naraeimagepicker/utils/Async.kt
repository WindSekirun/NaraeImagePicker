package com.github.windsekirun.naraeimagepicker.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

//From Anko library

class AnkoAsyncContext

private val crashLogger = { throwable: Throwable -> throwable.printStackTrace() }

fun doAsync(
        exceptionHandler: ((Throwable) -> Unit)? = crashLogger,
        task: AnkoAsyncContext.() -> Unit
): Future<Unit> {
    val context = AnkoAsyncContext()
    return BackgroundExecutor.submit {
        return@submit try {
            context.task()
        } catch (thr: Throwable) {
            val result = exceptionHandler?.invoke(thr)
            if (result != null) result else Unit
        }
    }
}

internal object BackgroundExecutor {
    private var executor: ExecutorService =
            Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())

    fun <T> submit(task: () -> T): Future<T> = executor.submit(task)

}