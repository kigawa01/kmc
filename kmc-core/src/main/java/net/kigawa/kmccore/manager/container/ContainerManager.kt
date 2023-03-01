package net.kigawa.kmccore.manager.container

import net.kigawa.kmccore.concurrent.ConcurrentList
import net.kigawa.kmccore.manager.classes.PluginClassEntry
import net.kigawa.kmccore.manager.classes.PluginClassManager
import net.kigawa.kmccore.util.DependencyStack
import net.kigawa.kmccore.util.manager.Manager
import net.kigawa.kutil.unitapi.component.UnitContainer

class ContainerManager(
  private val classLoaderManager: PluginClassManager,
  private val rootContainer: UnitContainer,
): Manager<ContainerEntry>() {
  private val containerEntries = ConcurrentList<ContainerEntry>()
  
  fun loadContainer(pluginClassEntry: PluginClassEntry): ContainerEntry {
    return loadContainer(pluginClassEntry, DependencyStack())
  }
  
  @Synchronized
  fun loadContainer(pluginClassEntry: PluginClassEntry, stack: DependencyStack<PluginClassEntry>): ContainerEntry {
    val dependencies = pluginClassEntry.dependencies
      .map {dependency->
        containerEntries
          .firstOrNull {dependency.isAssignableFrom(it.pluginClassEntry.pluginClass)}
        ?: classLoaderManager.getEntries()
          .firstOrNull {dependency.isAssignableFrom(it.pluginClass)}
          ?.let {loadContainer(it, stack.add(pluginClassEntry))}
        ?: throw RuntimeException("depended plugin is not found($pluginClassEntry,dependency=$dependency)")
      }
    return ContainerEntry(pluginClassEntry, dependencies, this,rootContainer)
      .apply(entries::add)
  }
}
