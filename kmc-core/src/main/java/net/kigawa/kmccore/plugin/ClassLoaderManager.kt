package net.kigawa.kmccore.plugin

import net.kigawa.kmccore.concurrent.ConcurrentList
import net.kigawa.kutil.unitapi.annotation.Kunit
import java.io.File

@Kunit
class ClassLoaderManager {
  private val classLoaders = ConcurrentList<PluginClassLoader>()
  fun loadFile(pluginFile: File) {
    val classLoader = PluginClassLoader(this, pluginFile.toURI().toURL())
    classLoaders.add(classLoader)
  }
  
  fun loadAllFile(pluginDir: File) {
    if (pluginDir.isFile) loadFile(pluginDir)
    pluginDir.listFiles()?.forEach {
      if (it.isFile) loadFile(it)
      else loadAllFile(it)
    }
  }
  
  fun unLoadClassLoader(classLoader: PluginClassLoader) {
    classLoaders.remove(classLoader)
  }
  
  fun getClassLoaders(): MutableList<PluginClassLoader> {
    return classLoaders.toMutableList()
  }
  
  fun loadClass(name: String?): Class<*> {
    return loadClass(name, false)
  }
  
  fun loadClass(name: String?, resolve: Boolean, vararg ignoreLoader: PluginClassLoader): Class<*> {
    classLoaders.forEach {
      if (ignoreLoader.contains(it)) return@forEach
      try {
        return it.loadClass(name, resolve, false)
      } catch (_: ClassNotFoundException) {
      }
    }
    throw ClassNotFoundException(name)
  }
}