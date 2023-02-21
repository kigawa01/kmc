package net.kigawa.kmccore.util

import net.kigawa.kmccore.concurrent.ConcurrentList
import net.kigawa.kmccore.plugin.Plugin
import net.kigawa.kutil.log.log.KLogger
import java.util.concurrent.*

class AsyncExecutor(private val logger: KLogger): AutoCloseable {
  var executor: ExecutorService = Executors.newCachedThreadPool()
    private set
  private var timeOutSec = 1
  private val tasks = ConcurrentList<TaskEntry>()
  
  fun <T> submit(plugin: Plugin, callable: Callable<T>): Future<T> {
    val task = executor.submit<T> {
      val result = callable.call()
      cleanTask()
      result
    }
    tasks.add(TaskEntry(plugin, task))
    return task
  }
  
  private fun cleanTask() {
    tasks.forEach {
      if (!it.future.isDone) return@forEach
      tasks.remove(it)
    }
  }
  
  @Suppress("unused")
  fun execute(plugin: Plugin, runnable: Runnable) {
    submit(plugin, runnable::run)
  }
  
  override fun close() {
    executor.shutdown()
    executor.awaitTermination(timeOutSec.toLong(), TimeUnit.SECONDS)
  }
  
  fun waitTask(plugin: Plugin) {
    while (true) {
      cleanTask()
      try {
        tasks.first {it.plugin == plugin}?.future?.get() ?: break
      } catch (e: Throwable) {
        logger.warning(e)
      }
    }
  }
  
  fun waitTask() {
    while (true) {
      cleanTask()
      try {
        tasks.first {true}?.future?.get() ?: break
      } catch (e: Throwable) {
        logger.warning(e)
      }
    }
  }
  
  class TaskEntry(val plugin: Plugin, val future: Future<*>)
}