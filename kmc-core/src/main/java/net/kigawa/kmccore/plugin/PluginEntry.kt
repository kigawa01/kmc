package net.kigawa.kmccore.plugin

import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.Listener
import net.kigawa.kmccore.util.TaskExecutor
import net.kigawa.kutil.unitapi.component.UnitContainer

class PluginEntry(
  val plugin: Plugin,
  private val taskExecutor: TaskExecutor,
  private val container: UnitContainer,
  private val eventDispatcher: EventDispatcher,
) {
  var isEnable = false
    private set
  
  @Synchronized
  fun enable() {
    if (isEnable) return
    isEnable = true
    
    taskExecutor.start(plugin.getName())
    plugin.onEnable()
    container.getUnitList(Listener::class.java)
      .filter {it.plugin == plugin}
      .forEach(eventDispatcher::registerListener)
  }
  
  @Synchronized
  fun disable() {
    if (!isEnable) return
    isEnable = false
    
    plugin.onDisable()
    container.getUnitList(Listener::class.java)
      .filter {it.plugin == plugin}
      .forEach {eventDispatcher.unregisterListener(it)}
    taskExecutor.end(plugin.getName())
  }
}