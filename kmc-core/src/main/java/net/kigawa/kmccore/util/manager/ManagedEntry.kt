package net.kigawa.kmccore.util.manager

abstract class ManagedEntry<T: ManagedEntry<T>>(protected val manager: Manager<T>) {
  fun remove() {
    @Suppress("UNCHECKED_CAST")
    manager.remove(this as T)
  }
}