package net.kigawa.kmccore.plugin

import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.event.plugin.PluginEndEvent
import net.kigawa.kmccore.event.plugin.PluginStartEvent
import net.kigawa.kmccore.util.AsyncExecutor
import net.kigawa.kmccore.util.TaskExecutor
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.annotation.Kunit
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.registrar.ListRegistrar
import net.kigawa.kutil.unit.util.AnnotationUtil
import java.io.File
import java.io.FileFilter
import java.net.JarURLConnection
import java.util.*
import java.util.concurrent.Future

@Kunit
class Plugins(
  private val container: UnitContainer,
  private val eventDispatcher: EventDispatcher,
  private val logger: KLogger,
  private val taskExecutor: TaskExecutor,
  private val asyncExecutor: AsyncExecutor,
) {
  private val pluginDir: File = KutilFile.getRelativeFile("plugin")
  
  fun start() {
    taskExecutor.execute("load plugins") {
      loadPlugins()
    }
    taskExecutor.execute("run tasks") {
      startPlugins().forEach {it?.get()}
    }
    container.close()
  }
  
  private fun loadPlugins() {
    pluginDir.mkdirs()
    val files = pluginDir.listFiles(FileFilter {it.name.endsWith(".jar")}) ?: return
    container.getUnit(ListRegistrar::class.java).register(files.flatMap {
      loadPlugin(it)
    })
  }
  
  private fun loadPlugin(file: File): MutableList<UnitIdentify<out Any>> {
    val identifies = mutableListOf<UnitIdentify<out Any>>()
    val resource = file.toURI().toURL()
    
    try {
      container.getUnit(PluginClassLoader::class.java).addPlugin(file)
    } catch (e: Throwable) {
      logger.warning(e)
      return identifies
    }
    
    (resource.openConnection() as JarURLConnection).jarFile.use {jarFile->
      for (entry in Collections.list(jarFile.entries())) {
        var name = entry.name
        if (!name.endsWith(".class")) continue
        name = name.replace('/', '.').replace(".class$".toRegex(), "")
        try {
          val unitClass = Class.forName(name)
          if (AnnotationUtil.hasUnitAnnotation(unitClass))
            identifies.add(UnitIdentify(unitClass, AnnotationUtil.getUnitNameByAnnotation(unitClass)))
        } catch (e: Throwable) {
          logger.warning(e)
        }
      }
    }
    return identifies
  }
  
  private fun startPlugins(): List<Future<*>?> {
    return container.getUnitList(Plugin::class.java).map {
      asyncExecutor.submit {
        if (eventDispatcher.dispatch(PluginStartEvent(it)).cancel) return@submit
        taskExecutor.execute(it.getName()) {
          it.start()
        }
        eventDispatcher.dispatch(PluginEndEvent(it))
      }
    }
  }
}