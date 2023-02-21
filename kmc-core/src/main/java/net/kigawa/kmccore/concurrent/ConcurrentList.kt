package net.kigawa.kmccore.concurrent

import java.util.*

open class ConcurrentList<T: Any>(collection: Collection<T>): MutableList<T> {
  constructor(): this(mutableListOf<T>())
  
  private val listBox = ConcurrentBox(collection.toMutableList()) {LinkedList(it)}
  
  @Synchronized
  private fun <R> modifyList(task: (MutableList<T>)->R): R {
    return listBox.modify(task)
  }
  
  fun toMutableList(): MutableList<T> {
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
    get() = toMutableList().size
  
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
  
  override fun addAll(index: Int, elements: Collection<T>): Boolean {
    return modifyList {
      it.addAll(index, elements)
    }
  }
  
  override fun add(index: Int, element: T) {
    return modifyList {
      it.add(index, element)
    }
  }
  
  override fun get(index: Int): T {
    return toMutableList()[index]
  }
  
  override fun isEmpty(): Boolean {
    return toMutableList().isEmpty()
  }
  
  override fun iterator(): MutableIterator<T> {
    return toMutableList().iterator()
  }
  
  override fun listIterator(): MutableListIterator<T> {
    return toMutableList().listIterator()
  }
  
  override fun listIterator(index: Int): MutableListIterator<T> {
    return toMutableList().listIterator(index)
  }
  
  override fun removeAt(index: Int): T {
    return toMutableList().removeAt(index)
  }
  
  override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
    return toMutableList().subList(fromIndex, toIndex)
  }
  
  override fun set(index: Int, element: T): T {
    return modifyList {
      it.set(index, element)
    }
  }
  
  override fun retainAll(elements: Collection<T>): Boolean {
    return toMutableList().retainAll(elements)
  }
  
  override fun removeAll(elements: Collection<T>): Boolean {
    return modifyList {
      it.removeAll(elements)
    }
  }
  
  override fun lastIndexOf(element: T): Int {
    return toMutableList().lastIndexOf(element)
  }
  
  override fun indexOf(element: T): Int {
    return toMutableList().indexOf(element)
  }
  
  override fun containsAll(elements: Collection<T>): Boolean {
    return toMutableList().containsAll(elements)
  }
  
  override fun contains(element: T): Boolean {
    return toMutableList().contains(element)
  }
  
  override fun toString(): String {
    return "ConcurrentList(${listBox.get()})"
  }
}