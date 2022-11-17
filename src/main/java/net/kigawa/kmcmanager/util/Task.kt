package net.kigawa.kmcmanager.util

import java.util.concurrent.Callable
import java.util.logging.Logger

class Task(var logger: Logger?) {
    constructor(): this(null)
    
    init {
    }
    
    //
//    fun <T> runAsync(name: String, callable: Callable<T>): Future<T> {
//        val result = executor?.submit(Callable {run(name, callable)})
//        if (result != null) return result
//        val futureTask = FutureTask(callable)
//        futureTask.run()
//        return futureTask
//    }
//
    fun <T> run(name: String, callable: Callable<T>): T {
        logger?.info("start $name")
        val result = callable.call()
        logger?.info("  end $name")
        return result
    }
}