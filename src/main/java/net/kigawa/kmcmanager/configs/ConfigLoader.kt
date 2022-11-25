package net.kigawa.kmcmanager.configs

interface ConfigLoader<T> {
    fun load(): T
}