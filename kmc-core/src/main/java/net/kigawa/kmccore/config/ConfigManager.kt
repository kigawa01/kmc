package net.kigawa.kmccore.config

import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.concurrent.ConcurrentList
import net.kigawa.kmccore.event.config.ConfigSetEvent
import net.kigawa.kutil.unitapi.annotation.Kunit

@Kunit
class ConfigManager(
  private val eventDispatcher: EventDispatcher,
) {
  private val list = ConcurrentList<ConfigEntry<out Any>>()
  
  fun <T: Any> set(key: ConfigKey<T, *>, value: T) {
    val entry = findEntry(key)
    
    val event = eventDispatcher.dispatch(ConfigSetEvent(key, entry.value, value))
    if (event.cancel) return
    
    synchronized(entry) {
      entry.value = event.changedValue
    }
  }
  
  fun <T: Any> addListener(
    key: ConfigKey<T, *>,
    isCall: Boolean,
    action: (T?, T?)->Unit,
  ): ConfigEntry.ConfigListenerTask<T> {
    return findEntry(key).addListener(isCall, action)
  }
  
  fun <T: Any> findEntry(id: String): ConfigEntry<T>? {
    @Suppress("UNCHECKED_CAST")
    return list.firstOrNull {it.key.id == id} as ConfigEntry<T>?
  }
  
  private fun <T: Any> findEntry(key: ConfigKey<T, *>): ConfigEntry<T> {
    @Suppress("UNCHECKED_CAST")
    var entry = list.firstOrNull {it.key == key} as ConfigEntry<T>?
    if (entry == null) {
      entry = ConfigEntry(key)
      list.add(entry)
    }
    return entry
  }
}
