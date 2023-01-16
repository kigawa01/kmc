package net.kigawa.kmccore.config

class SimpleConfigKey<T>(private val id: String, private val defaultValue: T): ConfigKey<T> {
  override fun getId(): String {
    return id
  }
  
  override fun getDefaultValue(): T {
    return defaultValue
  }
}