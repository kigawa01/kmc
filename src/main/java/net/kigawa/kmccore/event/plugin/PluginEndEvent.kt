package net.kigawa.kmccore.event.plugin

import net.kigawa.kmccore.plugin.Plugin

class PluginEndEvent(
    override val plugin: Plugin,
): PluginEvent {
}