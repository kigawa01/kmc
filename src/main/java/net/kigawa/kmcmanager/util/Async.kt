package net.kigawa.kmcmanager.util

import java.util.concurrent.*

class Async(private val task: Task): AutoCloseable {
    private var executor = Executors.newCachedThreadPool()
    
    fun <T> run(callable: Callable<T>): Future<T> {
        return executor.submit(callable)
    }
    
    fun  run(runnable: Runnable) {
        return executor.execute(runnable)
    }
    
    fun <T: Any> runTask(name: String, callable: Callable<T>): Future<T> {
        return run(Callable {task.run(name, callable)})
    }
    
    override fun close() {
        executor.shutdownNow()
    }
}