package net.kigawa.kmccore

import net.kigawa.kmccore.initfilter.PluginFilter
import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kmccore.plugin.Plugins
import net.kigawa.kmccore.util.AsyncExecutor
import net.kigawa.kmccore.util.TaskExecutor
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.log.log.fomatter.KFormatter
import net.kigawa.kutil.unitapi.component.InitializedFilterComponent
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.registrar.ClassRegistrar
import net.kigawa.kutil.unitapi.registrar.InstanceRegistrar
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

class KmcManager: AutoCloseable {
  val container: UnitContainer = UnitContainer.create()
  val preLoadPlugin = mutableListOf<Class<out Plugin>>()
  val classPath = mutableListOf<File>()
  var autoClose = true
  private var closed = true
  
  companion object {
    const val PROJECT_NAME = "kmc manager"
    
    @JvmStatic
    fun main(args: Array<String>) {
      KmcManager().start()
    }
  }
  
  @Synchronized
  fun start() {
    closed = false
    container.getUnit(InstanceRegistrar::class.java).register(initLogger())
    container.getUnit(ClassRegistrar::class.java).register(TaskExecutor::class.java)
    container.getUnit(TaskExecutor::class.java).execute(PROJECT_NAME) {
      init(container.getUnit(TaskExecutor::class.java))
      container.getUnit(Plugins::class.java).loadAll()
      container.getUnit(Plugins::class.java).enableAll()
    }
    if (!autoClose) return
    container.getUnit(AsyncExecutor::class.java).waitTask()
    container.close()
  }
  
  private fun init(taskExecutor: TaskExecutor) {
    taskExecutor.execute("init $PROJECT_NAME") {
      val classRegistrar = container.getUnit(ClassRegistrar::class.java)
      val instanceRegistrar = container.getUnit(InstanceRegistrar::class.java)
      classRegistrar.register(AsyncExecutor::class.java)
      instanceRegistrar.register(this)
      instanceRegistrar.register(container.getUnit(AsyncExecutor::class.java).executor)
      
      
      val packageName = javaClass.getPackage().name
      val classLoader = javaClass.classLoader
      
      container.getUnit(InitializedFilterComponent::class.java).add(PluginFilter::class.java)
    }
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
  
  override fun close() {
    synchronized(this) {
      if (closed) return
      closed = true
    }
    container.getUnit(Plugins::class.java).disableAll()
    container.close()
  }
}