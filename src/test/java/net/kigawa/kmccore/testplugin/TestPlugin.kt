package net.kigawa.kmccore.testplugin

import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kutil.unit.concurrent.ThreadLock
import java.util.concurrent.TimeUnit

class TestPlugin: Plugin {
  private val threadLock = ThreadLock()
  override fun start() {
    threadLock.block(1, TimeUnit.MINUTES)
  }
  
  override fun close() {
    threadLock.signeAll()
  }
}