package net.kigawa.kmccore.manager.classes

import net.kigawa.kmccore.manager.classloader.ClassLoaderEntry
import net.kigawa.kmccore.concurrent.ConcurrentSet
import net.kigawa.kmccore.manager.plugin.Plugin
import net.kigawa.kmccore.util.manager.ManagedEntry

class PluginClassesEntry(
  pluginClassManager: PluginClassManager,
  val classLoaderEntry: ClassLoaderEntry,
  val pluginClass: Class<out Plugin>,
): ManagedEntry<PluginClassesEntry>(pluginClassManager) {
  private val classes = ConcurrentSet<Class<out Any>>()
  private val children = ConcurrentSet<PluginClassesEntry>()
  
  init {
    addClass(pluginClass)
  }
  
  fun addClass(clazz: Class<out Any>) {
    children.forEach {
      if (it.isChildPackage(clazz)) {
        it.addClass(clazz)
        return
      }
    }
    classes.add(clazz)
  }
  
  fun addChild(pluginClassesEntry: PluginClassesEntry) {
    if (!isChildPackage(pluginClassesEntry)) throw RuntimeException("$pluginClassesEntry is not a child of $this")
    children.forEach {
      if (it.isChildPackage(pluginClassesEntry)) {
        it.addChild(pluginClassesEntry)
        return
      }
      if (pluginClassesEntry.isChildPackage(it)) {
        pluginClassesEntry.addChild(it)
        children.remove(it)
      }
    }
    children.add(pluginClassesEntry)
  }
  
  fun isChildPackage(packageName: String): Boolean {
    return packageName.startsWith(pluginClass.packageName)
  }
  
  fun isChildPackage(pluginClassesEntry: PluginClassesEntry): Boolean {
    return isChildPackage(pluginClassesEntry.pluginClass.packageName)
  }
  
  fun isChildPackage(clazz: Class<out Any>): Boolean {
    return isChildPackage(clazz.packageName)
  }
  
  override fun toString(): String {
    return "PluginClassesEntry(classLoaderManager=$manager, classloader=$classLoaderEntry, pluginClass=$pluginClass, classes=$classes, child=$children)"
  }
}