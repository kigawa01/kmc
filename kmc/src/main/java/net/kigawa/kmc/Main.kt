package net.kigawa.kmc

import net.kigawa.kmccoreapi.KmcManager

object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    KmcManager.create().start()
  }
}