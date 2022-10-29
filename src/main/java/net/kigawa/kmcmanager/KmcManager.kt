package net.kigawa.kmcmanager

import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.log.log.KLogger
import net.kigawa.kutil.unit.UnitContainer
import java.util.concurrent.Executors

class KmcManager
{
    private val executorService = Executors.newCachedThreadPool()
    private val logger: KLogger = KLogger(javaClass.name, null, null, KutilFile.getRelativeFile("log"))
    private val unitContainer: UnitContainer = UnitContainer(logger, executorService)

    init
    {
        try
        {
            unitContainer.loadUnits(javaClass)
        } catch (e: Throwable)
        {
            logger.warning(e.cause)
        }
    }
}