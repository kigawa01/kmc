package net.kigawa.kmccore.event.config

import net.kigawa.kmccore.AbstractTest
import net.kigawa.kmccore.EventDispatcher
import net.kigawa.kmccore.config.Configs
import net.kigawa.kmccore.config.SimpleConfigKey
import net.kigawa.testplugin.TestListener
import net.kigawa.testplugin.TestPlugin
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ConfigSetEventTest: AbstractTest() {
  private lateinit var configs: Configs
  private lateinit var eventDispatcher: EventDispatcher
  
  @Before
  fun before() {
    setUp()
    eventDispatcher = container.getUnit(EventDispatcher::class.java)
    configs = container.getUnit(Configs::class.java)
  }
  
  
  @Test
  fun dispatch() {
    val listener = TestListener(container.getUnit(TestPlugin::class.java))
    eventDispatcher.registerListener(listener)
    
    val key = SimpleConfigKey("id", "default")
    configs.set(key, TestPlugin::class.java, "value")
    
    assertEquals(key, listener.configSetEvent?.key)
    assertEquals("default", listener.configSetEvent?.beforeValue)
    assertEquals(TestPlugin::class.java, listener.configSetEvent?.pluginClass)
    assertEquals("value", listener.configSetEvent?.changedValue)
  }
  
  @After
  fun after() {
    tearDown()
  }
}