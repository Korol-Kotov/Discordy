package me.korolkotov.discordy.command

import me.korolkotov.discordy.load.LoadManagerInterface
import me.korolkotov.discordy.logger.Logger
import org.bukkit.Bukkit

class CommandManager : LoadManagerInterface<CommandManager> {
    override fun getInstance() = this

    override fun initialize() {
        val command = Bukkit.getPluginCommand("discord")
        if (command != null) {
            val executor = DiscordCommand()

            command.setExecutor(executor)
            command.tabCompleter = executor
            Logger.instance.debug("Command ${command.name} has been registered with executor ${executor::class.simpleName}.")
        }
    }
}