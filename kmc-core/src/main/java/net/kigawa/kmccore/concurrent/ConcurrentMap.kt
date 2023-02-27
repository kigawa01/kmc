package net.kigawa.kmccore.concurrent

open class ConcurrentMap<K, V>(map: Map<K, V>): MutableMap<K, V> {
  constructor(): this(mutableMapOf())
  
  private val listBox = ConcurrentBox(map.toMutableMap()) {HashMap(it)}
  
  @Synchronized
  private fun <R> modify(task: (MutableMap<K, V>)->R): R {
    return listBox.modify(task)
  }
  
  fun toMutableMap(): MutableMap<K, V> {
    return listBox.get()
  }
  
  override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
    get() = toMutableMap().entries
  override val keys: MutableSet<K>
    get() = toMutableMap().keys
  
  override val size: Int
    get() = toMutableMap().size
  override val values: MutableCollection<V>
    get() = toMutableMap().values
  
  override fun clear() {
    return modify {
      it.clear()
    }
  }
  
  override fun get(key: K): V? {
    return toMutableMap().get(key)
  }
  
  override fun containsValue(value: V): Boolean {
    return toMutableMap().containsValue(value)
  }
  
  override fun containsKey(key: K): Boolean {
    return toMutableMap().containsKey(key)
  }
  
  override fun isEmpty(): Boolean {
    return toMutableMap().isEmpty()
  }
  
  override fun remove(key: K): V? {
    return modify {
      it.remove(key)
    }
  }
  
  override fun putAll(from: Map<out K, V>) {
    return modify {
      it.putAll(from)
    }
  }
  
  override fun put(key: K, value: V): V? {
    return modify {
      it.put(key, value)
    }
  }
  
  override fun toString(): String {
    return "ConcurrentMap(entries=$entries)"
  }
}