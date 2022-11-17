package net.kigawa.kmcmanager.plugin

import net.kigawa.kutil.kutil.KutilFile
import java.io.File

class Plugins
{
    private val pluginDir: File = KutilFile.getRelativeFile("plugin")

    init
    {
        val files = pluginDir.listFiles()

    }
}