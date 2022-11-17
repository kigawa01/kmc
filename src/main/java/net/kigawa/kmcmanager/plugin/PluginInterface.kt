package net.kigawa.kmcmanager.plugin

import net.kigawa.kmcmanager.configs.Config

interface PluginInterface
{
    fun configurePlugin(config: Config)
}