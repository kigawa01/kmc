package net.kigawa.kmcmanager.config

import net.kigawa.kutil.unit.concurrent.ConcurrentList

class ConfigEntry<T: Any>(
  val key: ConfigKey<T>,
  var value: T,
) {
  constructor(key: ConfigKey<T>): this(key, key.getDefaultValue())
  
  private val listeners = ConcurrentList<(T)->Unit>()
  
  @Synchronized
  fun addListener(action: (T)->Unit): Boolean {
    return listeners.add(action)
  }
  
  @Synchronized
  fun removeListener(action: (T)->Unit): Boolean {
    return listeners.remove(action)
  }
  
  fun getListeners(): List<(T)->Unit> {
    return listeners.toMutableList()
  }
}