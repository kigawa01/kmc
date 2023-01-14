package net.kigawa.kmccore.util

import java.util.concurrent.*

class AsyncExecutor: AutoCloseable {
  var executor: ExecutorService = Executors.newCachedThreadPool()
    private set
  private var timeOutSec = 1
  
  fun <T> submit(callable: Callable<T>): Future<T> {
    return executor.submit(callable)
    }
  
  fun execute(runnable: Runnable) {
    executor.execute(runnable)
  }
  
  override fun close() {
    executor.shutdown()
    executor.awaitTermination(timeOutSec.toLong(), TimeUnit.SECONDS)
  }
}