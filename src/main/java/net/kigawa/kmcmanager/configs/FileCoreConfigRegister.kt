package net.kigawa.kmcmanager.configs

import net.kigawa.kutil.unit.annotation.Unit

@Unit
class FileCoreConfigRegister: ConfigSaver<CoreConfig>, ConfigLoader<CoreConfig> {
    private var coreConfig = CoreConfig()
    override fun load(): CoreConfig {
        return coreConfig.clone() as CoreConfig
    }
    
    override fun save(config: CoreConfig) {
        coreConfig = config.clone() as CoreConfig
    }
}