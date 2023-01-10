package net.kigawa.kmcmanager

import net.kigawa.kmcmanager.plugin.Plugins
import net.kigawa.kmcmanager.util.Async
import net.kigawa.kmcmanager.util.Task
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
    val task = Task(logger)
    val async = Async()
    container = UnitContainer.create()
    container.getUnit(InstanceRegistrar::class.java).register(async)
    container.getUnit(InstanceRegistrar::class.java).register(task)
    container.getUnit(InstanceRegistrar::class.java).register(logger)
    async.executor.let {container.getUnit(InstanceRegistrar::class.java).register(it)}
    container.getUnit(UnitAsyncComponent::class.java).add(ExecutorServiceExecutor::class.java)
    task.execute(PROJECT_NAME) {
      initProject(logger, task)
      container.getUnit(Plugins::class.java).start()
    }
  }
  
  private fun initProject(logger: KLogger, task: Task) {
    task.execute("set formatter") {
      Logger.getLogger("").handlers.forEach {
        it.formatter = KFormatter()
      }
    }
    task.execute("load classes") {
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