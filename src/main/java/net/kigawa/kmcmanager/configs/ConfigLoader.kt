package net.kigawa.kmcmanager.configs

interface ConfigLoader: ConfigRegister {
    fun <T>load(configClass: Class<T>): T
}