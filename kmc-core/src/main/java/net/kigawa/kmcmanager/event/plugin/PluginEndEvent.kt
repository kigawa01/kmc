package net.kigawa.kmcmanager.event.plugin

import net.kigawa.kmcmanager.plugin.Plugin

class PluginEndEvent(
    override val plugin: Plugin,
): PluginEvent {
}