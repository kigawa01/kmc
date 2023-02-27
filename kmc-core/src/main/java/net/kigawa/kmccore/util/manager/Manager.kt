package net.kigawa.kmccore.util.manager

import net.kigawa.kmccore.concurrent.ConcurrentList

abstract class Manager<T: ManagedEntry<T>> {
  protected val entries = ConcurrentList<T>()
  
  open fun remove(entry: T) {
    entries.remove(entry)
  }
}