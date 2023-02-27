package net.kigawa.kmccore.manager.plugin

import net.kigawa.kmccore.*
import net.kigawa.kmccore.manager.classloader.PluginClassLoader
import net.kigawa.kmccore.concurrent.ConcurrentList
import net.kigawa.kmccore.event.plugin.PluginEndEvent
import net.kigawa.kmccore.event.plugin.PluginStartEvent
import net.kigawa.kmccore.util.*
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.Kunit
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.registrar.ListRegistrar
import net.kigawa.kutil.unitapi.util.AnnotationUtil
import java.io.File
import java.io.FileFilter
import java.net.JarURLConnection
import java.util.*

@Kunit
class PluginManager(
  private val container: UnitContainer,
  private val eventDispatcher: EventDispatcher,
  private val logger: KLogger,
  private val taskExecutor: TaskExecutor,
  private val asyncExecutor: AsyncExecutor,
  private val kmcManager: KmcManagerImpl,
) {
  private val pluginDir: File = KutilFile.getRelativeFile("plugin")
  private val pluginEntries = ConcurrentList<PluginEntry>()
  fun unloadAll() {
  }
  
  fun unload(plugin: Plugin) {
  }
  
  fun enableAll() {
    taskExecutor.start("plugins")
    pluginEntries.map {it.plugin}.map {
      enablePlugin(it)
    }
  }
  
  fun enablePlugin(plugin: Plugin) {
    asyncExecutor.submit(plugin) {
      if (eventDispatcher.dispatch(PluginStartEvent(plugin)).cancel) return@submit
      val entry = getEntry(plugin)
      entry.enable()
    }
  }
  
  fun disableAll() {
    container.getUnitList(Plugin::class.java).map {
      disablePlugin(it)
    }
    taskExecutor.end("plugins")
  }
  
  fun disablePlugin(plugin: Plugin) {
    if (eventDispatcher.dispatch(PluginEndEvent(plugin)).cancel) return
    eventDispatcher.unregisterListener(plugin)
    plugin.onDisable()
    taskExecutor.end(plugin.getName())
    asyncExecutor.waitTask(plugin)
  }
  
  @Synchronized
  private fun getEntry(plugin: Plugin): PluginEntry {
    var entry = pluginEntries.first {it.plugin == plugin}
    if (entry != null) return entry
    entry = PluginEntry(plugin)
    pluginEntries.add(entry)
    return entry
  }
  
  fun loadPlugin(plugin: Plugin) {
    pluginEntries.add(PluginEntry(plugin))
  }
  
  private fun loadJars() {
    pluginDir.mkdirs()
    val files = pluginDir.listFiles(FileFilter {it.name.endsWith(".jar")}) ?: return
    container.getUnit(ListRegistrar::class.java).register(files.flatMap {
      loadJar(it)
    })
  }
  
  private fun loadJar(file: File): MutableList<UnitIdentify<out Any>> {
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
}