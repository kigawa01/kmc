package net.kigawa.kmccore.configlistener

import net.kigawa.kmccore.config.ConfigManager
import net.kigawa.kmccore.config.DefaultStringListConfigKey

class PluginLoadConfigListener(private val configManager: ConfigManager) {
  init {
    configManager.addListener(DefaultStringListConfigKey.PLUGIN_DIR, true) {before, after->
      val removed = before?.toMutableList()
      after?.let {removed?.removeAll(it)}
      
      val added = after?.toMutableList()
      before?.let {added?.removeAll(it)}
      
    }
  }
  
  fun loadDir(){
  
  }
  fun unLoadDir(){
    
  }
}