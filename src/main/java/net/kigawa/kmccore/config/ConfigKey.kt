package net.kigawa.kmccore.config

interface ConfigKey<T> {
  fun getId(): String
  fun getDefaultValue(): T
}