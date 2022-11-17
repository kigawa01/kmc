package net.kigawa.kmcmanager.util

import java.util.concurrent.Callable

class ErrorBundler {
    private val errors = mutableListOf<Throwable>()
    
    fun tryCatch(runnable: Runnable) {
        tryCatch(null) {runnable.run()}
    }
    
    fun <T: Any?> tryCatch(default: T, callable: Callable<T>): T {
        return try {
            callable.call()
        } catch (e: Throwable) {
            synchronized(this) {
                errors.add(e)
            }
            default
        }
    }
    
    @Synchronized
    fun clearError(action: (Throwable)->Unit) {
        errors.forEach(action)
        
        errors.clear()
    }
}