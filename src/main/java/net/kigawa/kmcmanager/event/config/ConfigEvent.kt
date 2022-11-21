package net.kigawa.kmcmanager.event.config

import net.kigawa.kmcmanager.configs.Config
import net.kigawa.kmcmanager.event.Event

interface ConfigEvent: Event {
    val config: Config
}