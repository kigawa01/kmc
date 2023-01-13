package net.kigawa.kmcmanager.initfilter

import net.kigawa.kmcmanager.EventDispatcher
import net.kigawa.kmcmanager.Listener
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.component.InitStack

class EventListenerFilter(
  private val eventDispatcher: EventDispatcher,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    if (obj !is Listener) return obj
    eventDispatcher.registerListener(obj)
    return obj
  }
}