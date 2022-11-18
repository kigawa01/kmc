package net.kigawa.kmcmanager.util

import java.util.concurrent.*

class Async(private val task: Task): AutoCloseable {
    private var executor: ExecutorService? = Executors.newCachedThreadPool()
    private var timeOutSec = 1
    
    fun <T> run(callable: Callable<T>): Future<T> {
        val executor = this.executor
        if (executor != null) return executor.submit(callable)
        val futureTask = FutureTask(callable)
        futureTask.run()
        return futureTask
    }
    
    fun run(runnable: Runnable) {
        executor?.execute(runnable) ?: runnable.run()
    }
    
    fun <T: Any> runTask(name: String, callable: Callable<T>): Future<T> {
        return run(Callable {task.run(name, callable)})
    }
    
    override fun close() {
        val executor = this.executor
        this.executor = null
        executor?.shutdown()
        executor?.awaitTermination(timeOutSec.toLong(), TimeUnit.SECONDS)
    }
}