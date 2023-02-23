package net.kigawa.kmccore.config

import net.kigawa.kmccore.plugin.Plugin

interface ConfigKey<T: Any, P: Plugin> {
  val id: String
  val defaultValue: T
  val pluginClass: Class<P>
}