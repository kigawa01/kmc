package net.kigawa.kmcmanager.util

import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.container.UnitContainer
import java.util.concurrent.Callable
import java.util.concurrent.Future

@Unit
class PluginDispatcher(
    private val logger: KLogger,
    private val container: UnitContainer,
    private val async: Async,
) {
    fun <T> execute(callable: Callable<T>): T? {
        return try {
            callable.call()
        } catch (e: Throwable) {
            logger.warning(e)
            container.removeUnit(e.javaClass)
            null
        }
    }
    
    fun <T> executeAsync(callable: Callable<T>): Future<T?> {
        return async.submit {execute(callable)}
    }
}