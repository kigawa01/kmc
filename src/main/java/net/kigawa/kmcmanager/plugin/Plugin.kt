package net.kigawa.kmcmanager.plugin

import net.kigawa.kmcmanager.configs.Config

interface Plugin: AutoCloseable {
    fun configurePlugin(config: Config)
}