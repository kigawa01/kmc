package net.kigawa.kmccore.config

import net.kigawa.kmccore.manager.plugin.Plugin

enum class DefaultStringListConfigKey(
  override val id: String,
  override val defaultValue: List<String>,
): ConfigKey<List<String>, Plugin> {
  PLUGIN_DIR("plugin-dir", listOf());
  
  override val pluginClass = Plugin::class.java
}