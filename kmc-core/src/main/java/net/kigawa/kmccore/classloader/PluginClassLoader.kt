package net.kigawa.kmccore.classloader

import net.kigawa.kutil.unitapi.annotation.Kunit
import java.net.URL
import java.net.URLClassLoader

@Kunit
class PluginClassLoader(private val classLoaderManager: ClassLoaderManager, url: URL):
  URLClassLoader(arrayOf(url)) {
  override fun loadClass(name: String?, resolve: Boolean): Class<*> {
    try {
      return normalLoadClass(name, resolve)
    } catch (_: ClassNotFoundException) {
    }
    try {
      return classLoaderManager.loadClass(name, resolve, this)
    } catch (_: ClassNotFoundException) {
    }
    
    throw ClassNotFoundException(name)
  }
  
  fun normalLoadClass(name: String?): Class<*> {
    return super.loadClass(name, false)
  }
  
  fun normalLoadClass(name: String?, resolve: Boolean): Class<*> {
    return super.loadClass(name, resolve)
  }
}