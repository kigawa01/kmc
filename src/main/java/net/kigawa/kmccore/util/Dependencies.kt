package net.kigawa.kmccore.util

import net.kigawa.kutil.unit.exception.UnitException
import java.util.*

class Dependencies<T>(private val findDependency: (T)->List<T>) {
  fun findAllDependencies(item: T, stack: DependencyStack<T>): List<T> {
    val stack1 = stack.add(item)
    val list = mutableListOf<T>()
    findDependency(item).forEach {
      list.add(it)
      list.addAll(findAllDependencies(it, stack1))
    }
    return list
  }
}

class DependencyStack<T>(
  private val stack: List<T>,
) {
  constructor(): this(mutableListOf())
  
  fun add(dependency: T): DependencyStack<T> {
    if (stack.contains(dependency)) throw UnitException("unit has bean circular reference", dependency)
    val list = LinkedList(stack)
    list.add(dependency)
    return DependencyStack(stack)
  }
}