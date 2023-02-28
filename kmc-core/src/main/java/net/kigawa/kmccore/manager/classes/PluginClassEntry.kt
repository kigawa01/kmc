package net.kigawa.kmccore.manager.classes

import net.kigawa.kmccore.concurrent.ConcurrentSet
import net.kigawa.kmccore.manager.classloader.ClassLoaderEntry
import net.kigawa.kmccore.manager.plugin.Plugin
import net.kigawa.kmccore.util.manager.ManagedEntry
import net.kigawa.kutil.unitapi.annotation.Dependencies

class PluginClassEntry(
  pluginClassManager: PluginClassManager,
  val classLoaderEntry: ClassLoaderEntry,
  val pluginClass: Class<out Plugin>,
): ManagedEntry<PluginClassEntry>(pluginClassManager) {
  private val classes = ConcurrentSet<Class<out Any>>()
  private val children = ConcurrentSet<PluginClassEntry>()
  val dependencies = pluginClass.getAnnotation(Dependencies::class.java).value
    .map {it.value.java}
    .filter {Plugin::class.java.isAssignableFrom(it)}
    .map {it.asSubclass(Plugin::class.java)}
  val name = pluginClass.simpleName
  
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
  
  fun addChild(pluginClassEntry: PluginClassEntry) {
    if (!isChildPackage(pluginClassEntry)) throw RuntimeException("$pluginClassEntry is not a child of $this")
    children.forEach {
      if (it.isChildPackage(pluginClassEntry)) {
        it.addChild(pluginClassEntry)
        return
      }
      if (pluginClassEntry.isChildPackage(it)) {
        pluginClassEntry.addChild(it)
        children.remove(it)
      }
    }
    children.add(pluginClassEntry)
  }
  
  fun isChildPackage(packageName: String): Boolean {
    return packageName.startsWith(pluginClass.packageName)
  }
  
  fun isChildPackage(pluginClassEntry: PluginClassEntry): Boolean {
    return isChildPackage(pluginClassEntry.pluginClass.packageName)
  }
  
  fun isChildPackage(clazz: Class<out Any>): Boolean {
    return isChildPackage(clazz.packageName)
  }
  
  override fun toString(): String {
    return "PluginClassesEntry(classLoaderManager=$manager, classloader=$classLoaderEntry, pluginClass=$pluginClass, classes=$classes, child=$children)"
  }
}