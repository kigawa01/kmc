package net.kigawa.kmccore.container

import net.kigawa.kmccore.classloader.ClassLoaderEntry
import net.kigawa.kmccore.classloader.ClassLoaderManager
import net.kigawa.kmccore.concurrent.ConcurrentList
import net.kigawa.kutil.unitapi.component.UnitContainer

class ContainerManager(
  private val classLoaderManager: ClassLoaderManager,
  private val rootContainer: UnitContainer
) {
  private val containerEntryList = ConcurrentList<ContainerEntry>()
  
  init {
  }
  
  fun loadContainer(classLoaderEntry: ClassLoaderEntry) {
  }
  
  fun createContainer(classes: List<Class<*>>) {
    val container = UnitContainer.create(rootContainer)
  }
}

