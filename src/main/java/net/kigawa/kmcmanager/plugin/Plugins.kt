package net.kigawa.kmcmanager.plugin

import net.kigawa.kmcmanager.factory.PluginFactory
import net.kigawa.kmcmanager.util.*
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
    private val async: Async,
    private val logger: KLogger,
    private val task: Task,
    private val pluginDispatcher: PluginDispatcher,
) {
    private val pluginDir: File = KutilFile.getRelativeFile("plugin")
    
    fun start() {
        container.addFactory(PluginFactory())
        task.execute("load plugins") {
            loadPlugins()
        }
        task.execute("run tasks") {
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
                task.execute(it.getName()) {
                    it.start()
                }
            }
        }
    }
}