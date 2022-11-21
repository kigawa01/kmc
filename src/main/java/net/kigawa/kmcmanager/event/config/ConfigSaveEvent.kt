package net.kigawa.kmcmanager.event.config

import net.kigawa.kmcmanager.configs.Config
import net.kigawa.kmcmanager.event.CancelableEvent

class ConfigSaveEvent(override val config: Config): ConfigEvent, CancelableEvent {
    override var cancel: Boolean = false
}