package net.kigawa.kmcmanager.configs

import net.kigawa.kutil.kutil.KutilFile
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.container.UnitContainer
import org.yaml.snakeyaml.Yaml

@Unit
class FileCoreConfigRegister(unitContainer: UnitContainer): ConfigSaver, ConfigLoader {
    private var coreConfig = CoreConfig()
    
    @Suppress("UNCHECKED_CAST")
    override fun <T> load(configClass: Class<T>): T {
        val yaml = Yaml()
        val stream = KutilFile.getFile(ConfigRegister.configDir, configClass.simpleName)
            .inputStream()
        val map = yaml.loadAs(stream, Map::class.java)
        
        
        
        configClass.fields.forEach {
            it.set()
        }
        
        return coreConfig.clone() as T
    }
    
    fun save(config: CoreConfig) {
        coreConfig = config.clone() as CoreConfig
    }
    
    override fun <T> save(config: T) {
        TODO("Not yet implemented")
    }
}