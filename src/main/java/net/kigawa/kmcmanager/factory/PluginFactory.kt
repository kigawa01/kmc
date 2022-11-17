package net.kigawa.kmcmanager.factory

import net.kigawa.kmcmanager.configs.Config
import net.kigawa.kmcmanager.plugin.Plugin
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.factory.DefaultFactory
import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.util.Util

class PluginFactory: UnitFactory {
    override fun init(unitClass: Class<*>, unitContainer: UnitContainer): Any {
        if (!isValid(unitClass)) throw Exception("$unitClass is not valid")
        val factory = unitContainer.getUnit(DefaultFactory::class.java)
        val unit = factory.init(unitClass, unitContainer) as Plugin
        unit.configurePlugin(unitContainer.getUnit(Config::class.java))
        return unit
    }
    
    override fun isValid(unitClass: Class<*>): Boolean {
        if (unitClass.isInterface) return false
        return Util.instanceOf(unitClass, Plugin::class.java)
    }
}