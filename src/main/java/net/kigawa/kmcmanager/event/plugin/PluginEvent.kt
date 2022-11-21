package net.kigawa.kmcmanager.event.plugin

import net.kigawa.kmcmanager.event.Event
import net.kigawa.kmcmanager.plugin.Plugin

interface PluginEvent: Event {
    val plugin: Plugin
}