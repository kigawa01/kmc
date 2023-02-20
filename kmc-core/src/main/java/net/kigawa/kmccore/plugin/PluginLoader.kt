package net.kigawa.kmccore.plugin

import net.kigawa.kutil.unitapi.registrar.ResourceRegistrar
import java.io.File

class PluginLoader {
  fun load(pluginFile: File) {
  }
  
  fun loadAll(pluginDir: File) {
    taskExecutor.execute("load all plugins") {
      kmcManager.preLoadPlugin.forEach {container.getUnit(ResourceRegistrar::class.java).register(it)}
      loadJars()
    }
  }
  
  fun unloadAll() {
  }
  
  fun unload(plugin: Plugin) {
  }
}