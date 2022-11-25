package net.kigawa.kmcmanager.configs

interface ConfigSaver<T> {
    fun save(config: T)
}