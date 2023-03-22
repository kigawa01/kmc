package net.kigawa.kmccore.util.manager

import net.kigawa.kmccore.concurrent.ConcurrentList

abstract class RemoveAble(
    val parent: RemoveAble?
) {
    private val removeTask = ConcurrentList<Runnable>()
    private val registeredTask = parent?.addRemoveTask(::remove)
    fun addRemoveTask(runnable: Runnable): Runnable {
        removeTask.add(runnable)
        return runnable
    }

    fun removeRemoveTask(runnable: Runnable) {
        removeTask.remove(runnable)
    }

    open fun remove() {
        registeredTask?.let { parent?.removeRemoveTask(it) }
        removeTask.forEach {
            it.run()
        }
    }
}