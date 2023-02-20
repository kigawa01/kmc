package net.kigawa.kmccore.util

import net.kigawa.kutil.unit.util.Util

class DependencyCirculationException: RuntimeException {
  constructor(message: String, cause: Throwable?, vararg obj: Any?): super(
    Util.createMessage(message, obj.toList()),
    cause
  )
  
  constructor(message: String, vararg obj: Any?): this(message, null, *obj)
}