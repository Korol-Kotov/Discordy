package me.korolkotov.discordy.discord

import kotlinx.coroutines.launch
import me.korolkotov.discordy.config.ConfigManager
import me.korolkotov.discordy.coroutine.PluginCoroutineScope
import me.korolkotov.discordy.database.DatabaseManager
import me.korolkotov.discordy.database.repository.DiscordRepository
import me.korolkotov.discordy.discord.model.DiscordUser
import me.korolkotov.discordy.load.LoadManager
import me.korolkotov.discordy.load.LoadManagerInterface
import me.korolkotov.discordy.logger.Logger
import me.korolkotov.discordy.util.TimeUtil
import net.dv8tion.jda.api.entities.User
import org.bukkit.entity.Player
import java.util.UUID

class DiscordManager : LoadManagerInterface<DiscordManager> {
    private val users = mutableListOf<DiscordUser>()

    private lateinit var bot: Bot

    lateinit var repository: DiscordRepository private set

    override fun getInstance() = this

    override fun initialize() {
        bot = Bot(ConfigManager.instance.config.bot.token)

        repository = LoadManager.getInstance(DatabaseManager::class.java).discordRepository

        PluginCoroutineScope.scope.launch {
            repository.userDao.findAll().forEach { user ->
                users.add(user)
            }
        }
    }

    fun getDiscordUser(uniqueId: UUID) = users.firstOrNull { it.uniqueId == uniqueId }

    fun createDiscordUser(player: Player, user: User) {
        val dsUser = getDiscordUser(player.uniqueId)
        if (dsUser != null) removeDiscordUser(dsUser)

        val discordUser = DiscordUser(
            player.uniqueId,
            player.name,
            user.idLong,
            0,
            null,
            TimeUtil.now()
        )
        users.add(discordUser)
        Logger.instance.debug("Created a new discord user. (name: ${discordUser.name}, uuid: ${discordUser.uniqueId}, discord_id: ${discordUser.discordId})")
        PluginCoroutineScope.scope.launch { repository.userDao.upsert(discordUser) }
    }

    fun removeDiscordUser(user: DiscordUser) {
        users.remove(user)
        PluginCoroutineScope.scope.launch {
            repository.userDao.delete(user.uniqueId)
        }
    }
}