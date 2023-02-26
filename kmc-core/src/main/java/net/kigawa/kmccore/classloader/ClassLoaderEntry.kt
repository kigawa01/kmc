package net.kigawa.kmccore.classloader

class ClassLoaderEntry(
  private val classLoaderManager: ClassLoaderManager,
  val classLoader: ClassLoader,
  val classList: List<Class<*>>,
) {
  constructor(classLoaderManager: ClassLoaderManager, classLoader: ClassLoader): this(
    classLoaderManager,
    classLoader,
    ClassLoadUtil.getClasses(classLoader)
  )
  
  fun remove() {
    classLoaderManager.removeEntry(this)
  }
  
  fun loadClass(name: String?): Class<*> {
    return if (classLoader is PluginClassLoader) classLoader.normalLoadClass(name)
    else classLoader.loadClass(name)
  }
}