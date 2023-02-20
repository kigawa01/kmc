package net.kigawa.kmccore.util

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.extention.UnitFinder
import net.kigawa.kutil.unitapi.options.FindOptions

class OptionalUnitFinder(private val container: UnitContainer): UnitFinder {
  override fun <T: Any> findUnits(identify: UnitIdentify<T>, findOptions: FindOptions): List<T>? {
    TODO("Not yet implemented")
  }
}