package net.kigawa.kmccore.config

import net.kigawa.kmccore.manager.plugin.Plugin

interface ConfigKey<T: Any, P: Plugin> {
  val id: String
  val defaultValue: T
  val pluginClass: Class<P>
}