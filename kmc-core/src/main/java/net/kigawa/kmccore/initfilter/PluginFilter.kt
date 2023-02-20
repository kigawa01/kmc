package net.kigawa.kmccore.initfilter

import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kmccore.plugin.PluginManager
import net.kigawa.kutil.unit.api.component.UnitInjectorComponent
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.component.InitStack

class PluginFilter(
  private val injectorComponent: UnitInjectorComponent,
  private val eventDispatcher: EventDispatcher,
  private val pluginManager: PluginManager,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    if (obj is Plugin) return plugin(obj)
    return obj
  }
  
  private fun <T: Plugin> plugin(plugin: T): T {
    pluginManager.loadPlugin(plugin)
    return plugin
  }
}