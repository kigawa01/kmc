package net.kigawa.kmcmanager.configs

import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.util.Util
import java.lang.reflect.Method

@Unit
class ConfigsImpl: Configs {
    private val savers = mutableListOf<ConfigSaver<out Any>>()
    private val loaders = mutableListOf<ConfigLoader<out Any>>()
    
    @Suppress("UNCHECKED_CAST")
    override fun save(config: Any) {
        sortListByFunc(savers, "save") {
            if (it.parameterTypes.size != 1) false
            else Util.instanceOf(config.javaClass, it.parameterTypes[0])
        }.forEach {
            (it as ConfigSaver<Any>).save(config)
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <T> loadConfigs(configClass: Class<T>): List<T> {
        return sortListByFunc(loaders, "load") {
            Util.instanceOf(it.returnType, configClass)
        }.map {
            it.load() as T
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    override fun <T> loadConfig(configClass: Class<T>): T {
        return sortListByFunc(loaders, "load") {
            Util.instanceOf(it.returnType, configClass)
        }.last().load() as T
    }
    
    private fun <T: Any> sortListByFunc(
        list: MutableList<T>,
        name: String,
        predicate: (Method)->Boolean,
    ): List<T> {
        return list.filter {obj->
            obj.javaClass.methods.filter {it.name == name}.any(predicate)
        }
    }
    
    override fun registerLoader(loader: ConfigLoader<out Any>) {
        loaders.add(loader)
    }
    
    override fun registerSaver(saver: ConfigSaver<out Any>) {
        savers.add(saver)
    }
}