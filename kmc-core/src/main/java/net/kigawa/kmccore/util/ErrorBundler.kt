package net.kigawa.kmccore.util

import java.util.concurrent.Callable

class ErrorBundler {
    private val errors = mutableListOf<Throwable>()
    
    @Synchronized
    fun addAll(errors: List<Throwable>) {
        this.errors.addAll(errors)
    }
    
    @Synchronized
    fun add(error: Throwable) {
        errors.add(error)
    }
    
    fun tryCatch(runnable: Runnable) {
        tryCatch(null) {runnable.run()}
    }
    
    fun tryAddAll(callable: Callable<List<Throwable>>) {
        addAll(tryCatch(listOf(), callable))
    }
    
    fun <T: Any?> tryCatch(default: T, callable: Callable<T>): T {
        return try {
            callable.call()
        } catch (e: Throwable) {
            add(e)
            default
        }
    }
    
    @Synchronized
    fun clearError(action: (Throwable)->Unit) {
        errors.forEach(action)
        
        errors.clear()
    }
}