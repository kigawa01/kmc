package net.kigawa.kmccore.event.plugin

import net.kigawa.kmccore.event.Event
import net.kigawa.kmccore.manager.plugin.Plugin

interface PluginEvent: Event {
    val plugin: Plugin
}