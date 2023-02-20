package net.kigawa.kmccore

import net.kigawa.kmccore.plugin.PluginManager
import net.kigawa.kmccore.util.AsyncExecutor
import net.kigawa.kmccore.util.TaskExecutor
import net.kigawa.kmccoreapi.KmcManager
import net.kigawa.kmccoreapi.KmcManager.Companion.PROJECT_NAME
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.log.log.fomatter.KFormatter
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.registrar.InstanceRegistrar
import net.kigawa.kutil.unitapi.registrar.ResourceRegistrar
import java.util.logging.Level
import java.util.logging.Logger

class KmcManagerImpl: KmcManager {
  private val container: UnitContainer = UnitContainer.create()
  var started = true
    private set
  
  override fun start() {
    synchronized(this) {
      if (started) return
      started = true
    }
    container.getUnit(InstanceRegistrar::class.java).let {
      it.register(initLogger())
      it.register(this)
    }
    container.getUnit(ResourceRegistrar::class.java).register(javaClass)
    container.getUnit(TaskExecutor::class.java).execute(PROJECT_NAME) {
      container.getUnit(PluginManager::class.java).loadAll()
      container.getUnit(PluginManager::class.java).enableAll()
    }
    container.getUnit(AsyncExecutor::class.java).waitTask()
    container.close()
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
      if (!started) return
      started = false
    }
    container.getUnit(PluginManager::class.java).disableAll()
    container.close()
  }
}