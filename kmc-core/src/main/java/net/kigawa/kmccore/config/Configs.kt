package net.kigawa.kmccore.config

import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.event.config.ConfigSetEvent
import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kutil.unitapi.annotation.Kunit

@Kunit
class Configs(
  private val eventDispatcher: EventDispatcher,
) {
  private val list = mutableListOf<ConfigEntry<out Any>>()
  fun <T: Any> get(key: ConfigKey<T>, pluginClass: Class<out Plugin>): T {
    return findEntry(key, pluginClass).value
  }
  
  fun <T: Any> set(key: ConfigKey<T>, pluginClass: Class<out Plugin>, value: T) {
    val entry = findEntry(key, pluginClass)
    
    val event = eventDispatcher.dispatch(ConfigSetEvent(key, pluginClass, entry.value, value))
    if (event.cancel) return
    
    synchronized(entry) {
      entry.getListeners().forEach {it(event.changedValue)}
      entry.value = event.changedValue
    }
  }
  
  fun <T: Any> addListener(key: ConfigKey<T>, pluginClass: Class<out Plugin>, action: (T)->Unit) {
    findEntry(key, pluginClass).addListener(action)
  }
  
  fun <T: Any> removeListener(key: ConfigKey<T>, pluginClass: Class<out Plugin>, action: (T)->Unit) {
    findEntry(key, pluginClass).removeListener(action)
  }
  
  fun <T: Any> getListeners(key: ConfigKey<T>, pluginClass: Class<out Plugin>): List<(T)->Unit> {
    return findEntry(key, pluginClass).getListeners()
  }
  
  private fun <T: Any> findEntry(key: ConfigKey<T>, pluginClass: Class<out Plugin>): ConfigEntry<T> {
    @Suppress("UNCHECKED_CAST")
    var entry = list.first {it.key == key.getId() && it.pluginClass == pluginClass} as ConfigEntry<T>?
    if (entry == null) {
      entry = ConfigEntry(key, pluginClass)
      list.add(entry)
    }
    return entry
  }
}
