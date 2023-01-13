package net.kigawa.kmcmanager

import net.kigawa.kmcmanager.initfilter.EventListenerFilter
import net.kigawa.kmcmanager.plugin.Plugins
import net.kigawa.kmcmanager.util.AsyncExecutor
import net.kigawa.kmcmanager.util.TaskExecutor
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.log.log.fomatter.KFormatter
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.extension.async.ExecutorServiceExecutor
import net.kigawa.kutil.unit.extension.registrar.*
import java.util.logging.Level
import java.util.logging.Logger

class KmcManager {
  private val container: UnitContainer = UnitContainer.create()
  
  companion object {
    const val PROJECT_NAME = "kmc manager"
    
    @JvmStatic
    fun main(args: Array<String>) {
      KmcManager()
    }
  }
  
  init {
    container.getUnit(InstanceRegistrar::class.java).register(initLogger())
    container.getUnit(ClassRegistrar::class.java).register(TaskExecutor::class.java)
    container.getUnit(TaskExecutor::class.java).execute(PROJECT_NAME) {
      init(container.getUnit(TaskExecutor::class.java))
      container.getUnit(Plugins::class.java).start()
    }
  }
  
  private fun init(taskExecutor: TaskExecutor) {
    val classRegistrar = container.getUnit(ClassRegistrar::class.java)
    classRegistrar.register(AsyncExecutor::class.java)
    container.getUnit(InstanceRegistrar::class.java).register(container.getUnit(AsyncExecutor::class.java).executor)
    container.getUnit(UnitAsyncComponent::class.java).add(ExecutorServiceExecutor::class.java)
    
    taskExecutor.execute("load classes") {
      container.getUnit(ResourceRegistrar::class.java).register(javaClass)
    }
    container.getUnit(InitializedFilterComponent::class.java).add(EventListenerFilter::class.java)
  }
  
  private fun initLogger(): KLogger {
    println("start init logger")
    val logger = KLogger("", null, Level.INFO, KutilFile.getRelativeFile("log"))
    logger.enable()
    Logger.getLogger("").handlers.forEach {
      it.formatter = KFormatter()
    }
    logger.info("end init logger")
    return logger
  }
}