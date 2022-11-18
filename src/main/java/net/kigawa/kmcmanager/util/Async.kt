package net.kigawa.kmcmanager.util

import java.util.concurrent.*

class Async: AutoCloseable {
    private var executor: ExecutorService? = Executors.newCachedThreadPool()
    private var timeOutSec = 1
    
    fun <T> submit(callable: Callable<T>): Future<T> {
        val executor = this.executor
        if (executor != null) return executor.submit(callable)
        val futureTask = FutureTask(callable)
        futureTask.run()
        return futureTask
    }
    
    fun execute(runnable: Runnable) {
        executor?.execute(runnable) ?: runnable.run()
    }
    
    override fun close() {
        val executor = this.executor
        this.executor = null
        executor?.shutdown()
        executor?.awaitTermination(timeOutSec.toLong(), TimeUnit.SECONDS)
    }
}