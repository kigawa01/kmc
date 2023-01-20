package net.kigawa.testplugin

import net.kigawa.kmccore.Listener
import net.kigawa.kmccore.annotation.EventHandler
import net.kigawa.kmccore.event.config.ConfigSetEvent
import net.kigawa.kmccore.plugin.Plugin

class TestListener: Listener {
  override val pluginClass: Class<out Plugin> = TestPlugin::class.java
  lateinit var testEvent: TestEvent
  
  @EventHandler
  fun testEvent(event: TestEvent) {
    this.testEvent = event
  }
  
  lateinit var configSetEvent: ConfigSetEvent<out Any>
  
  @EventHandler
  fun configSetEvent(event: ConfigSetEvent<out Any>) {
    this.configSetEvent = event
  }
}
