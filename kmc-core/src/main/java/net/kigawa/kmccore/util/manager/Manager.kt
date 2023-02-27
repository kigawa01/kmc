package net.kigawa.kmccore.util.manager

import net.kigawa.kmccore.concurrent.ConcurrentSet

abstract class Manager<T: ManagedEntry<T>> {
  protected val entries = ConcurrentSet<T>()
  
  open fun remove(entry: T) {
    entries.remove(entry)
  }
  
  fun getEntries(): MutableList<T> {
    return entries.toMutableList()
  }
}