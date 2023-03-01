package net.kigawa.kmccore.manager.container

import net.kigawa.kmccore.manager.classes.PluginClassEntry
import net.kigawa.kmccore.util.manager.ManagedEntry
import net.kigawa.kutil.unitapi.component.UnitContainer

class ContainerEntry(
  parent: PluginClassEntry,
  val dependencies: List<ContainerEntry>,
  manager: ContainerManager,
  rootContainer: UnitContainer,
): ManagedEntry<ContainerEntry, PluginClassEntry>(manager, parent) {
  val container: UnitContainer
  
  init {
    container = dependencies.map {it.container}.toTypedArray()
      .let {UnitContainer.create(this.parent.name, *it, rootContainer)}
  }
}