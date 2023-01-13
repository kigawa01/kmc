package net.kigawa.kmccore.config

import net.kigawa.kutil.unit.annotation.Kunit
import net.kigawa.kutil.unit.concurrent.ConcurrentList

@Kunit
class Configs {
  private val list = ConcurrentList<ConfigEntry<out Any>>()
  fun <T: Any> get(key: ConfigKey<T>): T {
    return findEntry(key).value
  }
  
  fun <T: Any> set(key: ConfigKey<T>, value: T) {
    val entry = findEntry(key)
    synchronized(entry) {
      entry.getListeners().forEach {it(value)}
      entry.value = value
    }
  }
  
  fun <T: Any> addListener(key: ConfigKey<T>, action: (T)->Unit) {
    findEntry(key).addListener(action)
  }
  
  fun <T: Any> removeListener(key: ConfigKey<T>, action: (T)->Unit) {
    findEntry(key).removeListener(action)
  }
  
  fun <T: Any> getListeners(key: ConfigKey<T>): List<(T)->Unit> {
    return findEntry(key).getListeners()
  }
  
  private fun <T: Any> findEntry(key: ConfigKey<T>): ConfigEntry<T> {
    @Suppress("UNCHECKED_CAST")
    var entry = list.first {it.key == key} as ConfigEntry<T>?
    if (entry == null) {
      entry = ConfigEntry(key)
      list.add(entry)
    }
    return entry
  }
}
