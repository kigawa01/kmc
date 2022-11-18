package net.kigawa.kmcmanager.util

import net.kigawa.kutil.kutil.StringColor
import java.util.concurrent.Callable
import java.util.logging.Logger

class Task(private val logger: Logger?) {
    fun <T> execute(name: String, callable: Callable<T>): T {
        logger?.info("${StringColor.GREEN}start${StringColor.RESET}> $name")
        val result = callable.call()
        logger?.info("${StringColor.BLUE}  end${StringColor.RESET}> $name")
        return result
    }
}