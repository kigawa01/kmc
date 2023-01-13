package net.kigawa.kmcmanager.plugin

interface Plugin: AutoCloseable {
    fun getName(): String
    fun start()
}