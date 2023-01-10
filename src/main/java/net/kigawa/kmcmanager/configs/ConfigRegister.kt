package net.kigawa.kmcmanager.configs

import java.io.File

interface ConfigRegister {
    companion object {
        @JvmStatic var configDir = File("config")
    }
}