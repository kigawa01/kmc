package net.kigawa.kmcmanager.factory

import net.kigawa.kmcmanager.configs.*
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.factory.DefaultFactory
import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.util.Util

class ConfigFactory: UnitFactory {
    override fun init(unitClass: Class<*>, unitContainer: UnitContainer): Any {
        val register = unitContainer.getUnit(DefaultFactory::class.java)
            .init(unitClass, unitContainer)
        val configs = unitContainer.getUnit(Configs::class.java)
        
        configs.addRegister(register as ConfigRegister)
        
        return register
    }
    
    override fun isValid(unitClass: Class<*>): Boolean {
        return Util.instanceOf(unitClass, ConfigRegister::class.java)
    }
}