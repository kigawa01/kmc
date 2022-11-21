package net.kigawa.kmcmanager.event.config

import net.kigawa.kmcmanager.configs.Config
import net.kigawa.kmcmanager.event.CancelableEvent

class ConfigLoadEvent(override val config: Config): ConfigEvent {
}