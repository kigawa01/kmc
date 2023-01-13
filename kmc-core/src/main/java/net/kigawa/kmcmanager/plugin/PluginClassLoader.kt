package net.kigawa.kmcmanager.plugin

import net.kigawa.kutil.unit.annotation.Unit
import java.io.File
import java.net.URLClassLoader

@Unit
class PluginClassLoader: URLClassLoader(arrayOf(), getSystemClassLoader()) {
    fun addPlugin(plugin: File) {
        addURL(plugin.toURI().toURL())
    }
}