package net.kigawa.kmcmanager.event

import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.util.Util
import java.lang.reflect.Method

@Unit
class Events(
    private val container: UnitContainer,
    private val logger: KLogger,
) {
    private val listenerFuncList = mutableListOf<ListenerFunc>()
    
    fun registerListener(listener: Listener) {
        listener.javaClass.methods.forEach {
            registerMethod(it, listener)
        }
    }
    
    private fun registerMethod(method: Method, listener: Listener) {
        val eventClasses = method.parameterTypes.forEach {
            if (!Util.instanceOf(it, CancelableEvent::class.java)) return@forEach
            synchronized(listenerFuncList) {
                listenerFuncList.add(ListenerFunc(listener, it, method))
            }
        }
    }
    
    fun <T: Event> dispatch(event: T): T {
        val funcList = synchronized(listenerFuncList) {
            listenerFuncList.filter {Util.instanceOf(event.javaClass, it.eventClass)}
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
            if (Util.instanceOf(event.javaClass, func.eventClass)) event
            else container.getUnit(it)
        }
        func.method.invoke(func.listener, params)
    }
}

class ListenerFunc(
    val listener: Listener,
    val eventClass: Class<*>,
    val method: Method,
) {
}