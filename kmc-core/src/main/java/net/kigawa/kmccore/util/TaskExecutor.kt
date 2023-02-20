package net.kigawa.kmccore.util

import net.kigawa.kutil.kutil.StringColor
import java.util.concurrent.Callable
import java.util.logging.Logger

class TaskExecutor(private val logger: Logger) {
  fun <T> execute(name: String, callable: Callable<T>): T {
    start(name)
    val result = callable.call()
    end(name)
    return result
  }
  
  fun start(name: String) {
    logger.info("${StringColor.GREEN}start${StringColor.RESET}> $name")
  }
  
  fun end(name: String) {
    logger.info("${StringColor.BLUE}  end${StringColor.RESET}> $name")
  }
}