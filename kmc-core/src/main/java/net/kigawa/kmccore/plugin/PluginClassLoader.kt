package net.kigawa.kmccore.plugin

import net.kigawa.kutil.unit.annotation.Kunit
import java.io.File
import java.net.URLClassLoader

@Kunit
class PluginClassLoader: URLClassLoader(arrayOf(), getSystemClassLoader()) {
  fun addPlugin(plugin: File) {
    addURL(plugin.toURI().toURL())
  }
}