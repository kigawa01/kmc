package net.kigawa.kmcmanager.config

interface ConfigKey<T> {
  fun getDefaultValue(): T
}