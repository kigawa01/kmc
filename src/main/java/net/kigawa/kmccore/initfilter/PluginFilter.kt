package net.kigawa.kmccore.initfilter

import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.Listener
import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.component.InitStack

class PluginFilter(
  private val databaseComponent: UnitDatabaseComponent,
  private val logger: KLogger,
  private val eventDispatcher: EventDispatcher,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    if (obj !is Plugin) return obj
    databaseComponent.findByClass(Listener::class.java).map {
      try {
        it.initOrGet(stack).get()
      } catch (e: Throwable) {
        logger.warning(e)
        null
      }
    }.filter {it?.pluginClass == obj.javaClass}.forEach {
      it?.let {it1-> eventDispatcher.registerListener(it1)}
    }
    return obj
  }
}