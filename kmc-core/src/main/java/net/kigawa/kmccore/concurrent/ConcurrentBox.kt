package net.kigawa.kmccore.concurrent

class ConcurrentBox<T>(private val item: T, private val clone: (T)->T) {
  @Synchronized
  fun <R> modify(task: (T)->R): R {
    return task(item)
  }
  
  @Synchronized
  fun get(): T {
    return clone.invoke(item)
  }
  
  fun clone(): ConcurrentBox<T> {
    return ConcurrentBox(get(), clone)
  }
}