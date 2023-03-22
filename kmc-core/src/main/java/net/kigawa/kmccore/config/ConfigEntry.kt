package net.kigawa.kmccore.config

import net.kigawa.kmccore.concurrent.ConcurrentList

class ConfigEntry<T: Any>(
  val key: ConfigKey<T, *>,
  value: T,
) {
  var value: T = value
    set(value) {
      listeners.forEach {it.call(field, value)}
      field = value
    }
  
  constructor(key: ConfigKey<T, *>): this(key, key.defaultValue)
  
  private val listeners = ConcurrentList<ConfigListenerTask<T>>()
  
  fun addListener(isCall: Boolean, action: (T?, T?)->Unit): ConfigListenerTask<T> {
    return ConfigListenerTask(this, action).apply(listeners::add).also {
      if (isCall) it.call(null, value)
    }
  }
  
  class ConfigListenerTask<T: Any>(private val configEntry: ConfigEntry<T>, private val action: (T?, T?)->Unit) {
    fun remove() {
      configEntry.listeners.remove(this)
    }
    
    fun call(before: T?, after: T?) {
      action(before, after)
    }
  }
}

