package net.kigawa.kmcmanager.configs

import net.kigawa.kmcmanager.event.Events
import net.kigawa.kmcmanager.event.config.ConfigLoadEvent
import net.kigawa.kmcmanager.event.config.ConfigSaveEvent
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.util.Util

@Unit
class Configs(
    private val logger: KLogger,
    private val events: Events,
) {
    private val registrationList = mutableListOf<ConfigRegistration<Config>>()
    
    fun save(config: Config) {
        if (events.dispatch(ConfigSaveEvent(config)).cancel) return
        val registration = synchronized(registrationList) {
            registrationList.filter {
                Util.instanceOf(config.javaClass, it.configClass)
            }
        }
        registration.forEach {
            try {
                it.saveConfig(config)
            } catch (e: Throwable) {
                logger.warning(e)
            }
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    fun <T: Config> loadConfig(configClass: Class<T>): T {
        val registration = synchronized(registrationList) {
            registrationList.find {
                Util.instanceOf(configClass, it.configClass)
            }
        }
        val config = registration?.loadConfig?.invoke() ?: throw Exception("config is not found")
        events.dispatch(ConfigLoadEvent(config))
        return config as T
    }
    
    fun <T: Config> registerConfig(configRegistration: ConfigRegistration<Config>) {
        synchronized(registrationList) {
            registrationList.add(configRegistration)
        }
    }
}

class ConfigRegistration<T: Config>(
    val configClass: Class<T>,
    val loadConfig: ()->T,
    val saveConfig: (T)->kotlin.Unit,
)