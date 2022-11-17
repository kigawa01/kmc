package net.kigawa.kmcmanager.plugin

import net.kigawa.kmcmanager.factory.PluginFactory
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.container.UnitContainer
import java.io.File

@Unit
class Plugins(private val unitContainer: UnitContainer) {
    private val pluginDir: File = KutilFile.getRelativeFile("plugin")
    
    init {
        val files = pluginDir.listFiles()
    }
    
    fun start() {
        unitContainer.addFactory(PluginFactory())
    }
}