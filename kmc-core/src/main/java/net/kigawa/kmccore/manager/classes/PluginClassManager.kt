package net.kigawa.kmccore.manager.classes

import net.kigawa.kmccore.concurrent.ConcurrentList
import net.kigawa.kmccore.manager.classloader.ClassLoaderEntry
import net.kigawa.kmccore.manager.plugin.Plugin
import net.kigawa.kmccore.util.manager.Manager
import net.kigawa.kutil.unitapi.annotation.Kunit
import java.lang.reflect.Modifier

@Kunit
class PluginClassManager: Manager<PluginClassEntry>() {
  @Synchronized
  fun loadClasses(classLoaderEntry: ClassLoaderEntry) {
    val pluginClasses = classLoaderEntry.classList
      .filter {!it.isInterface}
      .filter {Modifier.isAbstract(it.modifiers)}
      .filter {Plugin::class.java.isAssignableFrom(it)}
      .map {it.asSubclass(Plugin::class.java)}
    
    val pluginEntries = pluginClasses.map {PluginClassEntry(this, classLoaderEntry, it)}
    val result = ConcurrentList<PluginClassEntry>()
    pluginEntries.forEach {pluginClassesEntry->
      result.forEach resultLoop@{
        if (pluginClassesEntry.isChildPackage(it)) {
          pluginClassesEntry.addChild(it)
          return@resultLoop
        }
        if (it.isChildPackage(pluginClassesEntry)) {
          it.addChild(pluginClassesEntry)
        }
      }
      result.add(pluginClassesEntry)
    }
    
    classLoaderEntry.classList.forEach {clazz->
      result.forEach resultLoop@{
        if (it.isChildPackage(clazz)) {
          it.addClass(clazz)
          return@resultLoop
        }
      }
    }
    
    entries.addAll(result)
  }
  
  override fun remove(entry: PluginClassEntry) {
    super.remove(entry)
    
  }
}