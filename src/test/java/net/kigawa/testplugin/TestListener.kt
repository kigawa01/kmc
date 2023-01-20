package net.kigawa.testplugin

import net.kigawa.kmccore.Listener
import net.kigawa.kmccore.annotation.EventHandler
import net.kigawa.kmccore.event.config.ConfigSetEvent
import net.kigawa.kmccore.plugin.Plugin

class TestListener: Listener {
  override val pluginClass: Class<out Plugin> = TestPlugin::class.java
  var testEvent: TestEvent? = null
  var testPlugin: TestPlugin? = null
  
  @EventHandler
  fun testEvent(event: TestEvent, testPlugin: TestPlugin) {
    this.testEvent = event
    this.testPlugin = testPlugin
  }
  
  var configSetEvent: ConfigSetEvent<out Any>? = null
  
  @EventHandler
  fun configSetEvent(event: ConfigSetEvent<out Any>) {
    this.configSetEvent = event
  }
}
