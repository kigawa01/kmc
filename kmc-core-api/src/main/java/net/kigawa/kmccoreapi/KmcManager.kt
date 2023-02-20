package net.kigawa.kmccoreapi

interface KmcManager: AutoCloseable {
  companion object {
    const val PROJECT_NAME = "kmc manager"
    
    @JvmStatic
    fun create(): KmcManager {
      return Class.forName("net.kigawa.kmccore.KmcManager").getConstructor().newInstance() as KmcManager
    }
  }
  
  fun start()
}