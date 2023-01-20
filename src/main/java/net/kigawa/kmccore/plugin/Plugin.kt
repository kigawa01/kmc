package net.kigawa.kmccore.plugin

interface Plugin {
  fun getName(): String {
    return javaClass.simpleName
  }
  
  fun start()
  fun end()
}