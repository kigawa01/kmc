package net.kigawa.kmccore.initfilter

import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.Listener
import net.kigawa.kutil.unit.api.component.UnitInjectorComponent
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify

class ListenerFilter(
  private val injectorComponent: UnitInjectorComponent,
  private val eventDispatcher: EventDispatcher,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    if (obj !is Listener) return obj
    injectorComponent.findUnitAsync(UnitIdentify(obj.pluginClass, null), stack).get()
    eventDispatcher.registerListener(obj)
    return obj
  }
}