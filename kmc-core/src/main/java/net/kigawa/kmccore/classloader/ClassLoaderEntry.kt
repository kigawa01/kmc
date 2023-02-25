package net.kigawa.kmccore.classloader

class ClassLoaderEntry(
  private val classLoaderManager: ClassLoaderManager,
  val classLoader: PluginClassLoader,
  val classList: List<Class<*>>,
) {
  constructor(classLoaderManager: ClassLoaderManager, classLoader: PluginClassLoader): this(
    classLoaderManager,
    classLoader,
    ClassLoadUtil.getClasses(classLoader)
  )
  
  fun remove() {
    classLoaderManager.removeEntry(this)
  }
}