package net.kigawa.kmccore.plugin

import net.kigawa.kmccore.*
import net.kigawa.kmccore.event.plugin.PluginEndEvent
import net.kigawa.kmccore.event.plugin.PluginStartEvent
import net.kigawa.kmccore.util.AsyncExecutor
import net.kigawa.kmccore.util.TaskExecutor
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.annotation.Kunit
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.registrar.ListRegistrar
import net.kigawa.kutil.unit.extension.registrar.ResourceRegistrar
import net.kigawa.kutil.unit.util.AnnotationUtil
import java.io.File
import java.io.FileFilter
import java.net.JarURLConnection
import java.util.*

@Kunit
class Plugins(
  private val container: UnitContainer,
  private val eventDispatcher: EventDispatcher,
  private val logger: KLogger,
  private val taskExecutor: TaskExecutor,
  private val asyncExecutor: AsyncExecutor,
  private val kmcManager: KmcManager,
) {
  private val pluginDir: File = KutilFile.getRelativeFile("plugin")
  private val plugins = ConcurrentList<Plugin>()
  
  @Synchronized
  fun start() {
    taskExecutor.execute("load plugins") {
      kmcManager.preLoadPlugin.forEach {container.getUnit(ResourceRegistrar::class.java).register(it)}
      loadJars()
    }
    taskExecutor.start("plugins")
    container.getUnitList(Plugin::class.java).map {
      enablePlugin(it)
    }
  }
  
  @Synchronized
  fun end() {
    container.getUnitList(Plugin::class.java).map {
      disablePlugin(it)
    }
    taskExecutor.end("plugins")
  }
  
  @Synchronized
  fun enablePlugin(plugin: Plugin) {
    asyncExecutor.submit(plugin) {
      if (eventDispatcher.dispatch(PluginStartEvent(plugin)).cancel) return@submit
      taskExecutor.start(plugin.getName())
      plugin.onEnable()
      container.getUnitList(Listener::class.java)
        .filter {it.plugin == plugin}
        .forEach(eventDispatcher::registerListener)
    }
  }
  
  @Synchronized
  fun disablePlugin(plugin: Plugin) {
    if (eventDispatcher.dispatch(PluginEndEvent(plugin)).cancel) return
    eventDispatcher.unregister(plugin)
    plugin.onDisable()
    taskExecutor.end(plugin.getName())
    asyncExecutor.waitTask(plugin)
  }
  
  fun loadPlugin(plugin: Plugin) {
    plugins.add(plugin)
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