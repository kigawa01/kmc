package net.kigawa.kmccore.classloader

import net.kigawa.kmccore.util.manager.Manager
import net.kigawa.kutil.unitapi.annotation.Kunit
import java.io.File

@Kunit
class ClassLoaderManager: Manager<ClassLoaderEntry>() {
  init {
    ClassLoaderEntry(
      this,
      ClassLoaderManager::class.java.classLoader,
      ClassLoadUtil.getClasses(ClassLoaderManager::class.java.classLoader) {!it.startsWith("net.kigawa.kmccore")}
    ).apply(entries::add)
  }
  
  fun loadFile(pluginFile: File): ClassLoaderEntry {
    val classLoader = PluginClassLoader(this, pluginFile.toURI().toURL())
    return ClassLoaderEntry(this, classLoader).apply(entries::add)
  }
  
  fun loadAllFile(pluginDir: File): List<ClassLoaderEntry> {
    if (pluginDir.isFile) loadFile(pluginDir)
    return pluginDir.listFiles()?.flatMap {
      if (it.isFile) listOf(loadFile(it))
      else loadAllFile(it)
    } ?: listOf()
  }
  
  fun getEntries(): MutableList<ClassLoaderEntry> {
    return entries.toMutableList()
  }
  
  fun loadClass(name: String?): Class<*> {
    return loadClass(name, false)
  }
  
  fun loadClass(name: String?, resolve: Boolean, vararg ignoreLoader: PluginClassLoader): Class<*> {
    entries.forEach {
      if (ignoreLoader.contains(it.classLoader)) return@forEach
      try {
        return it.loadClass(name)
      } catch (_: ClassNotFoundException) {
      }
    }
    throw ClassNotFoundException(name)
  }
}