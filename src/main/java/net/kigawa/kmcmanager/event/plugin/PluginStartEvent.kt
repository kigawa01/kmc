package net.kigawa.kmcmanager.event.plugin

import net.kigawa.kmcmanager.event.CancelableEvent
import net.kigawa.kmcmanager.plugin.Plugin

class PluginStartEvent(override val plugin: Plugin): PluginEvent, CancelableEvent {
    override var cancel: Boolean = false
}