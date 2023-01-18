package net.kigawa.kmccore

import net.kigawa.kmccore.annotation.EventHandler
import net.kigawa.kmccore.event.Event
import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kmccore.testplugin.TestPlugin
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EventDispatcherTest: AbstractTest() {
  private lateinit var eventDispatcher: EventDispatcher
  
  @Before
  fun before() {
    setUp()
    eventDispatcher = container.getUnit(EventDispatcher::class.java)
  }
  
  @After
  fun after() {
    tearDown()
  }
  
  @Test
  fun dispatch() {
    val listener = TestListener()
    val event = TestEvent()
    eventDispatcher.registerListener(listener)
    eventDispatcher.dispatch(event)
    assertSame(event, listener.event)
  }
}

class TestListener: Listener {
  override val pluginClass: Class<out Plugin> = TestPlugin::class.java
  lateinit var event: TestEvent
  
  @EventHandler
  fun configEvent(event: TestEvent) {
    this.event = event
  }
}

class TestEvent: Event