package net.kigawa.kmccore.util.manager

import net.kigawa.kmccore.concurrent.ConcurrentList

abstract class ManagedEntry<SELF: ManagedEntry<SELF, PARENT>, PARENT: RemoveAble?>(
  protected val manager: Manager<SELF>,
  val parent: PARENT,
): RemoveAble {
  private val removeTask = ConcurrentList<Runnable>()
  private val registeredTask = parent?.addRemoveTask(::remove)
  override fun addRemoveTask(runnable: Runnable): Runnable {
    removeTask.add(runnable)
    return runnable
  }
  
  override fun removeRemoveTask(runnable: Runnable) {
    removeTask.remove(runnable)
  }
  
  override fun remove() {
    @Suppress("UNCHECKED_CAST")
    manager.remove(this as SELF)
    registeredTask?.let {parent?.removeRemoveTask(it)}
    removeTask.forEach {
      it.run()
    }
  }
}