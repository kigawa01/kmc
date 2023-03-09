package net.kigawa.kmccore.util

import net.kigawa.kmccore.concurrent.ConcurrentMap
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.UnitContainer

class UnitPool(
  private val container: UnitContainer,
  private val units: ConcurrentMap<UnitIdentify<out Any>, Any>,
) {
  fun <T: Any> getUnit(identify: UnitIdentify<T>): T {
    units.entries.forEach {
      @Suppress("UNCHECKED_CAST")
      if (identify == it.key) return it.value as T
    }
    return container.getUnit(identify)
  }
  
  fun <T: Any> getUnit(unitClass: Class<T>): T {
    return getUnit(UnitIdentify(unitClass, null))
  }
  
  fun <T: Any> addUnit(identify: UnitIdentify<T>, unit: T) {
    units[identify] = unit
  }
}