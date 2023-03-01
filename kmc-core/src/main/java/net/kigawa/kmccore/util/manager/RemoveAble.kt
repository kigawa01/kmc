package net.kigawa.kmccore.util.manager

interface RemoveAble {
  fun addRemoveTask(runnable: Runnable): Runnable
  fun removeRemoveTask(runnable: Runnable)
  fun remove()
}