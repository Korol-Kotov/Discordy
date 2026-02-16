package me.korolkotov.discordy.database.dao

import me.korolkotov.discordy.discord.model.DiscordUser
import java.util.UUID

interface UserDao {
    fun upsert(member: DiscordUser)
    fun findByUuid(uuid: UUID): DiscordUser?
    fun findByName(name: String): DiscordUser?
    fun findByDiscord(discordId: Long): DiscordUser?
    fun findAll(): List<DiscordUser>
    fun delete(uuid: UUID)
}