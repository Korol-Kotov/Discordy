package me.korolkotov.discordy.code

import me.korolkotov.discordy.config.ConfigManager
import me.korolkotov.discordy.load.LoadManagerInterface
import me.korolkotov.discordy.util.TaskService
import net.dv8tion.jda.api.entities.User
import org.bukkit.entity.Player
import java.util.UUID

class CodeManager : LoadManagerInterface<CodeManager> {
    private val codes = mutableListOf<Code>()

    override fun getInstance() = this

    override fun initialize() {}

    fun getCode(player: Player) = codes.firstOrNull { it.name.equals(player.name, true) }

    fun createCode(user: User, name: String) = createCode(name, user.idLong)
    fun createCode(player: Player, discordId: Long) = createCode(player.name, discordId)

    fun createCode(name: String, discordId: Long): Code {
        val code = Code(name, discordId, generateCode())
        TaskService.runLater(UUID.randomUUID().toString(), ConfigManager.instance.config.bot.code.lifetime * 20L) {
            removeCode(code)
        }
        return code
    }

    fun removeCode(code: Code) {
        codes.remove(code)
    }

    private fun generateCode(): String {
        val length = ConfigManager.instance.config.bot.code.length
        val symbols = ConfigManager.instance.config.bot.code.symbols
        var code = StringBuilder()
        repeat(length) {
            code = code.append(symbols.random())
        }
        return code.toString().trim()
    }
}