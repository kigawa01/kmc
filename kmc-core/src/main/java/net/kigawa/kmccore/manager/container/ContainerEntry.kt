package net.kigawa.kmccore.manager.container

import net.kigawa.kmccore.manager.classes.PluginClassEntry
import net.kigawa.kmccore.util.manager.ManagedEntry
import net.kigawa.kutil.unitapi.component.UnitContainer

class ContainerEntry(
  val pluginClassEntry: PluginClassEntry,
  val dependencies: List<ContainerEntry>,
  manager: ContainerManager,
): ManagedEntry<ContainerEntry>(manager) {
  val container: UnitContainer
  
  init {
    container = UnitContainer.create(pluginClassEntry.name, *dependencies.map {it.container}.toTypedArray())
  }
}