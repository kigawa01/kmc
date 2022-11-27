package net.kigawa.kmcmanager.factory

import net.kigawa.kmcmanager.configs.*
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.factory.DefaultFactory
import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.util.Util

@Suppress("UNCHECKED_CAST")
class ConfigFactory: UnitFactory {
    override fun init(unitClass: Class<*>, unitContainer: UnitContainer): Any {
        val register = unitContainer.getUnit(DefaultFactory::class.java)
            .init(unitClass, unitContainer)
        val configs = unitContainer.getUnit(Configs::class.java)
        if (register is ConfigSaver<*>) {
            configs.registerSaver(register as ConfigSaver<out Any>)
        }
        if (register is ConfigSaver<*>) {
            configs.registerLoader(register as ConfigLoader<out Any>)
        }
        return register
    }
    
    override fun isValid(unitClass: Class<*>): Boolean {
        return Util.instanceOf(unitClass, ConfigRegister::class.java)
    }
}