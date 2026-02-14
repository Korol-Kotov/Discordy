package me.korolkotov.discordy.load

import me.korolkotov.discordy.command.CommandManager
import me.korolkotov.discordy.config.ConfigManager
import me.korolkotov.discordy.database.DatabaseManager
import me.korolkotov.discordy.logger.LoggerManager

class LoadManager {
    companion object {
        private val loadManagerInterfaces = mutableListOf<LoadManagerInterface<*>>()

        fun <T> getInstance(clazz: Class<T>): T {
            return loadManagerInterfaces.filterIsInstance(clazz).firstNotNullOfOrNull { (it as LoadManagerInterface<T>).getInstance() }!!
        }
    }

    init {
        loadManagerInterfaces.add(ConfigManager())
        loadManagerInterfaces.add(LoggerManager())
        loadManagerInterfaces.add(DatabaseManager())
        loadManagerInterfaces.add(CommandManager())
    }

    fun initialize() = loadManagerInterfaces.forEach { it.initialize() }

    fun terminate() = loadManagerInterfaces.reversed().forEach { it.terminate() }

    fun reload() = loadManagerInterfaces.reversed().forEach { it.reload() }
}