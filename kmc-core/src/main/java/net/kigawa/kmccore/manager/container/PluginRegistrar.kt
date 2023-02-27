package net.kigawa.kmccore.manager.container

import net.kigawa.kmccore.manager.classloader.ClassLoaderManager
import net.kigawa.kutil.unitapi.annotation.Kunit
import net.kigawa.kutil.unitapi.extention.UnitRegistrar
@Kunit
class PluginRegistrar(
  private val classLoaderManager: ClassLoaderManager
): UnitRegistrar {
  fun register(classLoader: ClassLoader) {
  }
}