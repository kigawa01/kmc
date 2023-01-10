package net.kigawa.kmcmanager

import net.kigawa.kmcmanager.plugin.Plugins
import net.kigawa.kmcmanager.util.AsyncExecutor
import net.kigawa.kmcmanager.util.TaskEecutor
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.log.log.fomatter.KFormatter
import net.kigawa.kutil.unit.api.component.UnitAsyncComponent
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.extension.async.ExecutorServiceExecutor
import net.kigawa.kutil.unit.extension.registrar.InstanceRegistrar
import net.kigawa.kutil.unit.extension.registrar.ResourceRegistrar
import java.util.logging.Level
import java.util.logging.Logger

class KmcManager {
  private val container: UnitContainer
  
  companion object {
    const val PROJECT_NAME = "kmc manager"
    
    @JvmStatic
    fun main(args: Array<String>) {
      KmcManager()
    }
  }
  
  init {
    val logger = initLogger()
    val taskExecutor = TaskEecutor(logger)
    val asyncExecutor = AsyncExecutor()
    container = UnitContainer.create()
    container.getUnit(InstanceRegistrar::class.java).register(asyncExecutor)
    container.getUnit(InstanceRegistrar::class.java).register(taskExecutor)
    container.getUnit(InstanceRegistrar::class.java).register(logger)
    asyncExecutor.executor.let {container.getUnit(InstanceRegistrar::class.java).register(it)}
    container.getUnit(UnitAsyncComponent::class.java).add(ExecutorServiceExecutor::class.java)
    taskExecutor.execute(PROJECT_NAME) {
      init(taskExecutor)
      container.getUnit(Plugins::class.java).start()
    }
  }
  
  private fun init(taskExecutor: TaskEecutor) {
    taskExecutor.execute("set formatter") {
      Logger.getLogger("").handlers.forEach {
        it.formatter = KFormatter()
      }
    }
    taskExecutor.execute("load classes") {
      container.getUnit(ResourceRegistrar::class.java).register(javaClass)
    }
  }
  
  private fun initLogger(): KLogger {
    println("start init logger")
    val logger = KLogger("", null, Level.INFO, KutilFile.getRelativeFile("log"))
    logger.enable()
    logger.info("end init logger")
    return logger
  }
}