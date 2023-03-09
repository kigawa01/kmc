package net.kigawa.kmccore.manager.plugin

import net.kigawa.kmccore.manager.container.ContainerEntry
import net.kigawa.kmccore.util.manager.Manager
import net.kigawa.kutil.unitapi.annotation.Kunit

@Kunit
class PluginManager: Manager<PluginEntry>() {
  fun loadPlugin(pluginContainerEntry: ContainerEntry): PluginEntry {
    return PluginEntry(
      this,
      pluginContainerEntry
    ).apply(entries::add)
  }
}