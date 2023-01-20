package net.kigawa.testplugin

import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kutil.unit.annotation.Kunit

@Kunit
class TestPlugin: Plugin {
  override fun start() {
  }
  
  override fun close() {
  }
}