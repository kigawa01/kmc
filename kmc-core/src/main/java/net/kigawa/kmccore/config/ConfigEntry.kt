package net.kigawa.kmccore.config

import net.kigawa.kmccore.plugin.Plugin

class ConfigEntry<T: Any>(
  val key: String,
  val pluginClass: Class<out Plugin>,
  var value: T,
) {
  constructor(key: ConfigKey<T>, pluginClass: Class<out Plugin>): this(key.getId(), pluginClass, key.getDefaultValue())
  
  private val listeners = mutableListOf<(T)->Unit>()
  
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