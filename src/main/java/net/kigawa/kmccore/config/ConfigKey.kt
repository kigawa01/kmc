package net.kigawa.kmccore.config

interface ConfigKey<T> {
  fun getDefaultValue(): T
}