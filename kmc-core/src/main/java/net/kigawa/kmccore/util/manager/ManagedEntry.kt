package net.kigawa.kmccore.util.manager

abstract class ManagedEntry<SELF : ManagedEntry<SELF, PARENT>, PARENT : RemoveAble?>(
  protected val manager: Manager<SELF>,
  override val parentField: PARENT,
) : RemoveAble(parentField) {

    override fun remove() {
        super.remove()
        @Suppress("UNCHECKED_CAST")
        manager.remove(this as SELF)
    }
}