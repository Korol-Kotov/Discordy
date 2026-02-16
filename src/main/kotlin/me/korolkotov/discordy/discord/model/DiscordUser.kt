package me.korolkotov.discordy.discord.model

import java.time.Instant
import java.util.UUID

data class DiscordUser(
    val uniqueId: UUID,
    val name: String,
    val discordId: Long,
    var minutes: Int,
    var rewardedAt: Instant?,
    val createdAt: Instant
)