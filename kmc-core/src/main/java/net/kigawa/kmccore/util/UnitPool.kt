package net.kigawa.kmccore.util

import net.kigawa.kmccore.concurrent.ConcurrentMap
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.ArgName
import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.component.UnitContainer
import java.lang.reflect.Constructor

class UnitPool(
    private val container: UnitContainer,
    private val units: ConcurrentMap<UnitIdentify<out Any>, Any>,
) {
    fun <T : Any> getUnit(identify: UnitIdentify<T>): T {
        units.entries.forEach {
            @Suppress("UNCHECKED_CAST")
            if (identify == it.key) return it.value as T
        }
        return container.getUnit(identify)
    }

    fun <T : Any> getUnit(unitClass: Class<T>): T {
        return getUnit(unitClass, null)
    }

    fun <T : Any> getUnit(unitClass: Class<T>, name: String?): T {
        return getUnit(UnitIdentify(unitClass, name))
    }

    fun <T : Any> addUnit(identify: UnitIdentify<T>, unit: T): UnitPool {
        units[identify] = unit
        return this
    }

    fun <T : Any> addUnit(unitClass: Class<T>?, name: String?, unit: T): UnitPool {
        return addUnit(UnitIdentify(unitClass ?: unit.javaClass, name), unit)
    }

    fun <T : Any> addUnit(name: String?, unit: T): UnitPool {
        return addUnit(null, name, unit)
    }

    fun <T : Any> addUnit(unitClass: Class<T>?, unit: T): UnitPool {
        return addUnit(unitClass, null, unit)
    }

    fun <T : Any> addUnit(unit: T): UnitPool {
        return addUnit(null, null, unit)
    }

    fun <T : Any> init(clazz: Class<T>): T {
        val constructor: Constructor<*> =
            if (clazz.declaredConstructors.size == 1) clazz.constructors[0]
            else clazz.declaredConstructors
                .filter { !it.isAnnotationPresent(Inject::class.java) }
                .also { if (it.size != 1) throw RuntimeException("constructor must be only one") }
                .firstOrNull()
                ?: throw RuntimeException("constructor not found")

        @Suppress("UNCHECKED_CAST")
        return constructor.parameters.map {
            getUnit(it.type, it.getAnnotation(ArgName::class.java)?.name)
        }.toTypedArray().let { constructor.newInstance(*it) } as T
    }
}