package net.kigawa.kmccore.classes

import net.kigawa.kmccore.classloader.ClassLoaderEntry
import net.kigawa.kmccore.concurrent.ConcurrentList
import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kmccore.util.manager.Manager
import net.kigawa.kutil.unitapi.annotation.Kunit
import java.lang.reflect.Modifier

@Kunit
class PluginClassManager: Manager<PluginClassesEntry>() {
  fun loadClasses(classLoaderEntry: ClassLoaderEntry) {
    val pluginClasses = classLoaderEntry.classList
      .filter {!it.isInterface}
      .filter {Modifier.isAbstract(it.modifiers)}
      .filter {Plugin::class.java.isAssignableFrom(it)}
      .map {it.asSubclass(Plugin::class.java)}
    
    val pluginEntries = pluginClasses.map {PluginClassesEntry(this, classLoaderEntry, it)}
    val result = ConcurrentList<PluginClassesEntry>()
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
}