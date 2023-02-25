package net.kigawa.kmccore.classloader

import net.kigawa.kmccore.classloader.ClassLoaderManager
import net.kigawa.kutil.unitapi.annotation.Kunit
import java.net.URL
import java.net.URLClassLoader

@Kunit
class PluginClassLoader(private val classLoaderManager: ClassLoaderManager, url: URL):
  URLClassLoader(arrayOf(url)) {
  override fun loadClass(name: String?, resolve: Boolean): Class<*> {
    return loadClass(name, resolve, true)
  }
  
  fun loadClass(name: String?, resolve: Boolean, findAll: Boolean): Class<*> {
    try {
      return super.loadClass(name, resolve)
    } catch (_: ClassNotFoundException) {
    }
    if (findAll) {
      return classLoaderManager.loadClass(name, resolve, this)
    }
    
    throw ClassNotFoundException(name)
  }
}