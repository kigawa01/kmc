package net.kigawa.kmccore.classloader

import net.kigawa.kutil.unitapi.exception.UnitException
import java.io.File
import java.net.JarURLConnection
import java.net.URL
import java.util.*
import java.util.jar.JarFile

object ClassLoadUtil {
  private const val JAR_PROTOCOL = "jar"
  private const val FILE_PROTOCOL = "file"
  fun getClasses(classLoader: ClassLoader, filter: (String)->Boolean = {true}): List<Class<*>> {
    return classLoader.getResources("").toList().flatMap {url->
      getClassnames(url, "").filter(filter).mapNotNull {
        try {
          classLoader.loadClass(it)
        } catch (_: ClassNotFoundException) {
          null
        } catch (e: Exception) {
          e.printStackTrace()
          null
        }
      }
    }
  }
  
  fun getClassnames(url: URL, packageName: String): List<String> {
    return when (url.protocol) {
      JAR_PROTOCOL ->(url.openConnection() as JarURLConnection).jarFile.use {getJarClassnames(it, packageName)}
      FILE_PROTOCOL->getFileClassnames(File(url.toURI()), packageName)
      else         ->throw RuntimeException("could not support resource protocol")
    }
  }
  
  fun getFileClassnames(dir: File, packageName: String): List<String> {
    val files = dir.listFiles() ?: throw UnitException("cold not load unit files")
    val result = mutableListOf<String>()
    files.forEach {file->
      if (file.isDirectory) {
        result.addAll(getFileClassnames(file, packageName + "." + file.name))
      }
      if (!file.name.endsWith(".class")) return@forEach
      var name = file.name
      name = name.replace(".class$".toRegex(), "")
      result.add("$packageName.$name")
    }
    return result
  }
  
  fun getJarClassnames(jarFile: JarFile, packageName: String): List<String> {
    return Collections.list(jarFile.entries()).mapNotNull {
      val name = it.name
      if (!name.startsWith(packageName.replace('.', '/'))) return@mapNotNull null
      if (!name.endsWith(".class")) return@mapNotNull null
      name.replace('/', '.').replace(".class$".toRegex(), "")
    }
  }
}