package net.kigawa.kmccore.manager.classloader

import net.kigawa.kmccore.util.manager.ManagedEntry
import net.kigawa.kmccore.util.manager.RemoveAble

class ClassLoaderEntry(
  classLoaderManager: ClassLoaderManager,
  val classLoader: ClassLoader,
  val classList: List<Class<*>>,
): ManagedEntry<ClassLoaderEntry, RemoveAble?>(classLoaderManager, null) {
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