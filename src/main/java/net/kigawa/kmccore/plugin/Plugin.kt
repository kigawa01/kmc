package net.kigawa.kmccore.plugin

interface Plugin: AutoCloseable {
    fun getName(): String
    fun start()
}