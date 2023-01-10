package net.kigawa.kmcmanager.configs

interface ConfigSaver: ConfigRegister {
    fun <T> save(config: T)
}