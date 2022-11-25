package net.kigawa.kmcmanager.configs

import net.kigawa.kutil.unit.annotation.Unit

@Unit
interface Configs {
    fun save(config: Any)
    fun <T> loadConfigs(configClass: Class<T>): List<T>
   @Suppress("unused")
   fun <T> loadConfig(configClass: Class<T>): T {
        return loadConfigs(configClass).last()
    }
    fun registerLoader(loader: ConfigLoader<out Any>)
    fun registerSaver(saver: ConfigSaver<out Any>)
}