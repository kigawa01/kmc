package net.kigawa.kmccore.event.plugin

import net.kigawa.kmccore.event.CancelableEvent
import net.kigawa.kmccore.manager.plugin.Plugin

class PluginStartEvent(override val plugin: Plugin): PluginEvent, CancelableEvent {
    override var cancel: Boolean = false
}