package net.kigawa.kmcmanager.plugin

import net.kigawa.kmcmanager.EventDispatcher
import net.kigawa.kmcmanager.event.plugin.PluginEndEvent
import net.kigawa.kmcmanager.event.plugin.PluginStartEvent
import net.kigawa.kmcmanager.util.AsyncExecutor
import net.kigawa.kmcmanager.util.TaskExecutor
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.annotation.Kunit
import net.kigawa.kutil.unit.api.component.UnitContainer
import net.kigawa.kutil.unit.extension.registrar.JarRegistrar
import java.io.File
import java.io.FileFilter
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
    val classLoader = container.getUnit(PluginClassLoader::class.java)
    files.forEach {
      try {
        classLoader.addPlugin(it)
      } catch (e: Throwable) {
        logger.warning(e)
        return@forEach
      }
      container.getUnit(JarRegistrar::class.java).register(it.toURI().toURL(), "")
    }
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