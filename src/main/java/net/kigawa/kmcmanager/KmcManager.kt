package net.kigawa.kmcmanager

import net.kigawa.kmcmanager.plugin.Plugins
import net.kigawa.kmcmanager.util.*
import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.log.log.fomatter.KFormatter
import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.container.UnitContainer
import java.util.logging.Level
import java.util.logging.Logger

class KmcManager {
    private val errors = ErrorBundler()
    private val container: UnitContainer
    
    companion object {
        const val PROJECT_NAME = "kmc manager"
        
        @JvmStatic
        fun main(args: Array<String>) {
            KmcManager()
        }
    }
    
    init {
        val logger = initLogger()
        val task = Task(logger)
        val async = Async(task)
        container = UnitContainer.create(logger, task, async)
        container.executor = async::run
        task.run(PROJECT_NAME) {
            initProject(logger, task)
            container.getUnit(Plugins::class.java).start()
        }
    }
    
    fun initProject(logger: KLogger, task: Task) {
        task.run("set formatter") {
            Logger.getLogger("").handlers.forEach {
                it.formatter = KFormatter()
            }
        }
        task.run("load units") {
            errors.tryAddAll {
                container.registerUnits(ClassList.create(javaClass))
            }
        }
        task.run("init units") {
            errors.tryAddAll {
                container.initUnits()
            }
        }
        
        errors.clearError {logger.warning(it)}
    }
    
    fun initLogger(): KLogger {
        println("start init logger")
        val logger = KLogger("", null, Level.INFO, KutilFile.getRelativeFile("log"))
        logger.enable()
        logger.info("end init logger")
        return logger
    }
}