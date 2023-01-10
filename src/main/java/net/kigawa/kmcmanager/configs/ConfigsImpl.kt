package net.kigawa.kmcmanager.configs

import net.kigawa.kmcmanager.util.Async
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.util.Util
import java.lang.reflect.Method

@Unit
class ConfigsImpl(private val async: Async): Configs {
    private val savers = mutableListOf<ConfigSaver>()
    private val loaders = mutableListOf<ConfigLoader>()
    
    override fun save(config: Any) {
        sortListByFunc(savers, "save") {
            if (it.parameterTypes.size != 1) false
            else Util.instanceOf(config.javaClass, it.parameterTypes[0])
        }.forEach {
            async.execute {it.save(config)}
        }
    }
    
    override fun <T> loadConfigs(configClass: Class<T>): List<T> {
        return sortListByFunc(loaders, "load") {
            Util.instanceOf(it.returnType, configClass)
        }.map {loader->
            loader.load(configClass)
        }
    }
    
    override fun <T> loadConfig(configClass: Class<T>): T {
        return sortListByFunc(loaders, "load") {
            Util.instanceOf(it.returnType, configClass)
        }.last().load(configClass)
    }
    
    @Synchronized
    override fun addRegister(configRegister: ConfigRegister) {
        if (configRegister is ConfigSaver) savers.add(configRegister)
        if (configRegister is ConfigLoader) loaders.add(configRegister)
    }
    
    @Synchronized
    override fun removeRegister(configRegister: ConfigRegister) {
        savers.remove(configRegister)
        loaders.remove(configRegister)
    }
    
    @Synchronized
    private fun <T: Any> sortListByFunc(
        list: MutableList<T>,
        name: String,
        predicate: (Method)->Boolean,
    ): List<T> {
        return list.filter {obj->
            obj.javaClass.methods.filter {it.name == name}.any(predicate)
        }
    }
}