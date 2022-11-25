package net.kigawa.kmcmanager.factory

import net.kigawa.kmcmanager.event.Events
import net.kigawa.kmcmanager.event.Listener
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.factory.DefaultFactory
import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.util.Util

class EventFactory: UnitFactory {
    override fun init(unitClass: Class<*>, unitContainer: UnitContainer): Any {
        if (!isValid(unitClass)) throw Exception("$unitClass is not valid")
        val factory = unitContainer.getUnit(DefaultFactory::class.java)
        val listener = factory.init(unitClass, unitContainer) as Listener
        val events = unitContainer.getUnit(Events::class.java)
        events.registerListener(listener)
        return listener
    }
    
    override fun isValid(unitClass: Class<*>): Boolean {
        if (unitClass.isInterface) return false
        return Util.instanceOf(unitClass, Listener::class.java)
    }
}