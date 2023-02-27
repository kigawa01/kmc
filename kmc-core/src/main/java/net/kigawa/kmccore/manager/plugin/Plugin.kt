package net.kigawa.kmccore.manager.plugin

interface Plugin {
  fun getName(): String {
    return javaClass.simpleName
  }
  
  fun onEnable()
  fun onDisable()
}