package net.kigawa.kmccore.manager.container

import net.kigawa.kmccore.manager.classes.PluginClassEntry
import net.kigawa.kmccore.util.manager.ManagedEntry
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.registrar.SelectionRegistrar

class ContainerEntry(
  parent: PluginClassEntry,
  val dependencies: List<ContainerEntry>,
  manager: ContainerManager,
  rootContainer: UnitContainer,
): ManagedEntry<ContainerEntry, PluginClassEntry>(manager, parent) {
  val container: UnitContainer
  
  init {
    container = dependencies.map {it.container}.toTypedArray()
      .let {UnitContainer.create(parent.name, *it, rootContainer)}
    
    parent.getClasses().map {
      container.getUnit(SelectionRegistrar::class.java).selectRegister(it)
    }.forEach {it?.invoke()}
  }
}