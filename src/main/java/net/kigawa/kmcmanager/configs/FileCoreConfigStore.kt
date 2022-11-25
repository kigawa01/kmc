package net.kigawa.kmcmanager.configs

import net.kigawa.kutil.unit.annotation.Unit

@Unit
class FileCoreConfigStore: ConfigSaver<CoreConfig>, ConfigLoader<CoreConfig> {
    override fun load(): CoreConfig {
        TODO("Not yet implemented")
    }
    
    override fun save(config: CoreConfig) {
        TODO("Not yet implemented")
    }
}