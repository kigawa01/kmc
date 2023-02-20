package net.kigawa.kmccore.util

import java.util.*

open class ConcurrentList<T: Any>(private vararg val immutableItem: T) {
  private var list = listOf<T>()
    @Synchronized get() {
      return LinkedList(field)
    }
  
  open fun add(item: T): Boolean {
    return synchronized(this) {
      val l = list.toMutableList()
      val result = l.add(item)
      list = l
      result
    }
  }
  
  open fun remove(item: T): Boolean {
    return synchronized(this) {
      val l = list.toMutableList()
      val result = l.remove(item)
      list = l
      result
    }
  }
  
  @Suppress("unused")
  open fun removeLast(): T {
    return synchronized(this) {
      val l = list.toMutableList()
      val result = l.removeLast()
      list = l
      result
    }
  }
  
  fun last(predicate: (T)->Boolean): T? {
    for (item in reversed()) {
      if (predicate(item)) return item
    }
    return null
  }
  
  fun first(predicate: (T)->Boolean): T? {
    for (item in toMutableList()) {
      if (predicate(item)) return item
    }
    return null
  }
  
  fun filter(predicate: (T)->Boolean): List<T> {
    return toMutableList().filter(predicate)
  }
  
  fun reversed(): List<T> {
    return toMutableList().reversed()
  }
  
  fun toMutableList(): MutableList<T> {
    val result = mutableListOf(*immutableItem)
    result.addAll(list)
    return result
  }
  
  fun forEach(action: (T)->Unit) {
    toMutableList().forEach(action)
  }
  
  fun <R> flatMap(transform: (T)->Iterable<R>): List<R> {
    return toMutableList().flatMap(transform)
  }
  
  @Suppress("unused")
  fun contain(predicate: (T)->Boolean): Boolean {
    return first(predicate) != null
  }
  
  fun <R> map(transform: (T)->R): List<R> {
    return toMutableList().map(transform)
  }
  
  override fun toString(): String {
    return "ConcurrentList(immutableItem=${immutableItem.contentToString()}, list=$list)"
  }
}