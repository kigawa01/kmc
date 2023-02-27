package net.kigawa.kmccore.classloader

import net.kigawa.kmccore.util.manager.ManagedEntry

class ClassLoaderEntry(
  private val classLoaderManager: ClassLoaderManager,
  val classLoader: ClassLoader,
  val classList: List<Class<*>>,
) :ManagedEntry<ClassLoaderEntry>(classLoaderManager){
  constructor(classLoaderManager: ClassLoaderManager, classLoader: ClassLoader): this(
    classLoaderManager,
    classLoader,
    ClassLoadUtil.getClasses(classLoader)
  )
  fun loadClass(name: String?): Class<*> {
    return if (classLoader is PluginClassLoader) classLoader.normalLoadClass(name)
    else classLoader.loadClass(name)
  }
}