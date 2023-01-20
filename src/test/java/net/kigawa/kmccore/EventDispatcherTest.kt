package net.kigawa.kmccore

import net.kigawa.testplugin.TestEvent
import net.kigawa.testplugin.TestListener
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
    assertSame(event, listener.testEvent)
  }
}
