package net.kigawa.kmccore

import junit.framework.TestCase
import net.kigawa.kmccore.testplugin.TestPlugin

abstract class AbstractTest: TestCase() {
  val kmcManager = KmcManager()
  val container = kmcManager.container
  
  override fun setUp() {
    super.setUp()
    kmcManager.preLoadPlugin.add(TestPlugin::class.java)
    kmcManager.start()
  }
  
  override fun tearDown() {
    super.tearDown()
    kmcManager.close()
  }
}