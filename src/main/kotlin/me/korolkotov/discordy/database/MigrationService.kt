package me.korolkotov.discordy.database

import me.korolkotov.discordy.Main
import me.korolkotov.discordy.config.ConfigManager
import org.flywaydb.core.Flyway
import javax.sql.DataSource

class MigrationService(dataSource: DataSource) {
    private val flyway = Flyway.configure(Main.instance.javaClass.getClassLoader())
        .dataSource(dataSource)
        .baselineOnMigrate(true)
        .locations("classpath:db/migration/${ConfigManager.instance.databaseConfig.type}/")
        .mixed(true)
        .validateMigrationNaming(true)
        .load()

    fun migrate() {
        flyway.migrate()
    }
}