package net.kigawa.kmccore.manager.plugin

import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.Listener
import net.kigawa.kmccore.manager.container.ContainerEntry
import net.kigawa.kmccore.util.TaskExecutor
import net.kigawa.kmccore.util.manager.ManagedEntry
import net.kigawa.kmccore.util.manager.Manager

class PluginEntry(
  manager: Manager<PluginEntry>,
  parent: ContainerEntry,
): ManagedEntry<PluginEntry, ContainerEntry>(manager, parent) {
  val plugin = parent.container.getUnit(Plugin::class.java)
  private val taskExecutor = parent.container.getUnit(TaskExecutor::class.java)
  private val eventDispatcher = parent.container.getUnit(EventDispatcher::class.java)
  var isEnable = false
    private set
  
  @Synchronized
  fun enable() {
    if (isEnable) return
    isEnable = true
    
    taskExecutor.start(plugin.getName())
    plugin.onEnable()
    parent.container.getUnitList(Listener::class.java)
      .filter {it.plugin == plugin}
      .forEach(eventDispatcher::registerListener)
  }
  
  @Synchronized
  fun disable() {
    if (!isEnable) return
    isEnable = false
    
    plugin.onDisable()
    parent.container.getUnitList(Listener::class.java)
      .filter {it.plugin == plugin}
      .forEach {eventDispatcher.unregisterListener(it)}
    taskExecutor.end(plugin.getName())
  }
}