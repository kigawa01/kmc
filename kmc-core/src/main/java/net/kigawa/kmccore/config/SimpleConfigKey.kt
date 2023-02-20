package net.kigawa.kmccore.config

import java.util.*

class SimpleConfigKey<T>(private val id: String, private val defaultValue: T): ConfigKey<T> {
  override fun getId(): String {
    return id
  }
  
  override fun getDefaultValue(): T {
    return defaultValue
  }
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SimpleConfigKey<*>) return false
    if (javaClass != other.javaClass) return false
    
    if (id != other.id) return false
    if (defaultValue != other.defaultValue) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    return Objects.hash(id, defaultValue)
  }
}