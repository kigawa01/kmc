package net.kigawa.kmcmanager.plugin

import net.kigawa.kmcmanager.factory.PluginFactory
import net.kigawa.kmcmanager.util.*
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.container.UnitContainer
import java.io.File
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
    
    init {
        val files = pluginDir.listFiles()
    }
    
    fun start() {
        container.addFactory(PluginFactory())
        task.execute("run tasks") {
            runTasks()
        }.forEach {it?.get()}
        container.close()
    }
    
    private fun runTasks(): List<Future<*>?> {
        return container.getUnitList(Plugin::class.java).map {
            pluginDispatcher.executeAsync {
                task.execute(it.getName()) {
                    it.start()
                }
            }
        }
    }
}