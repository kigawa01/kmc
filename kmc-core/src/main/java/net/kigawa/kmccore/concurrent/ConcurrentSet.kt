package net.kigawa.kmccore.concurrent

open class ConcurrentSet<T: Any>(collection: Collection<T>): MutableSet<T> {
  constructor(): this(mutableListOf<T>())
  
  private val listBox = ConcurrentBox(collection.toMutableSet()) {HashSet(it)}
  
  @Synchronized
  private fun <R> modifyList(task: (MutableSet<T>)->R): R {
    return listBox.modify(task)
  }
  
  fun toMutableSet(): MutableSet<T> {
    return listBox.get()
  }
  
  override fun add(element: T): Boolean {
    return modifyList {
      it.add(element)
    }
  }
  
  override fun remove(element: T): Boolean {
    return modifyList {
      it.remove(element)
    }
  }
  
  override val size: Int
    get() = toMutableSet().size
  
  override fun clear() {
    return modifyList {
      it.clear()
    }
  }
  
  override fun addAll(elements: Collection<T>): Boolean {
    return modifyList {
      it.addAll(elements)
    }
  }
  
  override fun isEmpty(): Boolean {
    return toMutableSet().isEmpty()
  }
  
  override fun iterator(): MutableIterator<T> {
    return toMutableSet().iterator()
  }
  
  override fun retainAll(elements: Collection<T>): Boolean {
    return toMutableSet().retainAll(elements)
  }
  
  override fun removeAll(elements: Collection<T>): Boolean {
    return modifyList {
      it.removeAll(elements)
    }
  }
  
  override fun containsAll(elements: Collection<T>): Boolean {
    return toMutableSet().containsAll(elements)
  }
  
  override fun contains(element: T): Boolean {
    return toMutableSet().contains(element)
  }
  
  override fun toString(): String {
    return "ConcurrentList(${listBox.get()})"
  }
}