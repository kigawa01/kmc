package net.kigawa.kmccore.plugin

interface Plugin: AutoCloseable {
  fun getName(): String {
    return javaClass.simpleName
  }
  
  fun start()
}