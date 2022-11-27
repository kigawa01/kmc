package net.kigawa.kmcmanager.configs

interface ConfigLoader<T>: ConfigRegister {
    fun load(): T
}