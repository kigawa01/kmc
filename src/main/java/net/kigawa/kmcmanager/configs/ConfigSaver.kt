package net.kigawa.kmcmanager.configs

interface ConfigSaver<T>: ConfigRegister {
    fun save(config: T)
}