package net.kigawa.kmccore.util.manager

abstract class ManagedEntry<SELF : ManagedEntry<SELF, PARENT>, PARENT : RemoveAble?>(
  protected val manager: Manager<SELF>,
  parent: PARENT,
) : RemoveAble(parent) {

    override fun remove() {
        super.remove()
        @Suppress("UNCHECKED_CAST")
        manager.remove(this as SELF)
    }
}