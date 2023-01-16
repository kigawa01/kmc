package net.kigawa.kmccore.event.config

import net.kigawa.kmccore.config.ConfigKey
import net.kigawa.kmccore.event.CancelableEvent
import net.kigawa.kmccore.plugin.Plugin

@Suppress("unused")
class ConfigSetEvent<T: Any>(
  var key: ConfigKey<T>,
  var pluginClass: Class<out Plugin>,
  val beforeValue: T,
  val changedValue: T,
): CancelableEvent {
  override var cancel: Boolean = false
}