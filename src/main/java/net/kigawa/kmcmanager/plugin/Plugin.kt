package net.kigawa.kmcmanager.plugin

import net.kigawa.kmcmanager.configs.Config

interface Plugin: AutoCloseable {
    fun getName(): String
    fun configurePlugin(config: Config)
    fun start()
}