package net.kigawa.kmcmanager.plugin

import net.kigawa.kmcmanager.event.Events
import net.kigawa.kmcmanager.event.plugin.PluginEndEvent
import net.kigawa.kmcmanager.event.plugin.PluginStartEvent
import net.kigawa.kmcmanager.factory.PluginFactory
import net.kigawa.kmcmanager.util.PluginDispatcher
import net.kigawa.kmcmanager.util.TaskEecutor
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.classlist.JarfileClassList
import net.kigawa.kutil.unit.container.UnitContainer
import java.io.File
import java.io.FileFilter
import java.util.concurrent.Future

@Unit
class Plugins(
    private val container: UnitContainer,
    private val events: Events,
    private val logger: KLogger,
    private val taskEecutor: TaskEecutor,
    private val pluginDispatcher: PluginDispatcher,
) {
    private val pluginDir: File = KutilFile.getRelativeFile("plugin")
    
    fun start() {
        container.addFactory(PluginFactory())
        taskEecutor.execute("load plugins") {
            loadPlugins()
        }
        taskEecutor.execute("run tasks") {
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
            val classList = JarfileClassList(it)
            container.registerUnits(classList).forEach {e-> logger.warning(e)}
            container.initUnits().forEach {e-> logger.warning(e)}
        }
    }
    
    private fun startPlugins(): List<Future<*>?> {
        return container.getUnitList(Plugin::class.java).map {
            pluginDispatcher.executeAsync {
                if (events.dispatch(PluginStartEvent(it)).cancel) return@executeAsync
                taskEecutor.execute(it.getName()) {
                    it.start()
                }
                events.dispatch(PluginEndEvent(it))
            }
        }
    }
}