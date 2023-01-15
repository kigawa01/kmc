package net.kigawa.kmccore

import net.kigawa.kmccore.plugin.Plugin

interface Listener {
  val pluginClass: Class<out Plugin>
}