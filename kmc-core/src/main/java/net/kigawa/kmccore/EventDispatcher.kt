package net.kigawa.kmccore

import net.kigawa.kmccore.annotation.EventHandler
import net.kigawa.kmccore.event.Event
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unitapi.annotation.Kunit
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.util.ReflectionUtil
import java.lang.reflect.Method

@Kunit
class EventDispatcher(
  private val container: UnitContainer,
  private val logger: KLogger,
) {
  private val listenerFuncList = mutableListOf<ListenerFunc>()
  
  fun registerListener(listener: Listener) {
    listener.javaClass.methods.forEach {
      if (!it.isAnnotationPresent(EventHandler::class.java)) return@forEach
      registerMethod(it, listener)
    }
  }
  
  private fun registerMethod(method: Method, listener: Listener) {
    method.parameterTypes.forEach {
      if (!ReflectionUtil.instanceOf(it, Event::class.java)) return@forEach
      synchronized(listenerFuncList) {
        listenerFuncList.add(ListenerFunc(listener, it, method))
      }
    }
  }
  
  fun unregisterListener(listener: Listener) {
    synchronized(listenerFuncList) {
      listenerFuncList.forEach {
        if (it.listener != listener) return@forEach
        listenerFuncList.remove(it)
      }
    }
  }
  
  fun <T: Event> dispatch(event: T): T {
    val funcList = synchronized(listenerFuncList) {
      listenerFuncList.filter {ReflectionUtil.instanceOf(event.javaClass, it.eventClass)}
    }
    funcList.map {
      try {
        dispatch(event, it)
      } catch (e: Throwable) {
        logger.warning(e)
      }
    }
    return event
  }
  
  private fun dispatch(event: Event, func: ListenerFunc) {
    val params = func.method.parameterTypes.map {
      if (ReflectionUtil.instanceOf(event.javaClass, it)) event
      else container.getUnit(it)
    }
    try {
      func.method.invoke(func.listener, *params.toTypedArray())
    } catch (e: IllegalArgumentException) {
      logger.warning(e)
      logger.warning("params: $params")
    }
  }
  
  class ListenerFunc(
    val listener: Listener,
    val eventClass: Class<*>,
    val method: Method,
  )
}

