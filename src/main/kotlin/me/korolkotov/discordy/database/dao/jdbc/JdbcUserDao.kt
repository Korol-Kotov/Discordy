package me.korolkotov.discordy.database.dao.jdbc

import me.korolkotov.discordy.config.ConfigManager
import me.korolkotov.discordy.database.dao.UserDao
import me.korolkotov.discordy.discord.model.DiscordUser
import me.korolkotov.discordy.util.getInstant
import me.korolkotov.discordy.util.setInstant
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.UUID
import javax.sql.DataSource

class JdbcUserDao(
    private val ds: DataSource
) : UserDao {
    override fun upsert(member: DiscordUser) {
        ds.connection.use { con ->
            val sql = if (ConfigManager.instance.databaseConfig.type.equals("sqlite", true)) """
                INSERT INTO users
                (unique_id, name, discord_id, minutes, rewarded_at, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT(unique_id) DO UPDATE SET
                    name = excluded.name,
                    discord_id = excluded.discord_id,
                    minutes = excluded.minutes,
                    rewarded_at = excluded.rewarded_at
            """ else """
                INSERT INTO users
                (unique_id, name, discord_id, minutes, rewarded_at, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    discord_id = VALUES(discord_id),
                    minutes = VALUES(minutes),
                    rewarded_at = VALUES(rewarded_at)
            """.trimIndent()

            con.prepareStatement(sql).use { ps ->
                var i = 1
                ps.setString(i++, member.uniqueId.toString())
                ps.setString(i++, member.name)
                ps.setLong(i++, member.discordId)
                ps.setInt(i++, member.minutes)
                ps.setTimestamp(i++, member.rewardedAt?.let { Timestamp.from(it) })
                ps.setInstant(i, member.createdAt)

                ps.executeUpdate()
            }
        }
    }

    override fun findByUuid(uuid: UUID): DiscordUser? =
        ds.connection.use { con ->
            con.prepareStatement(
                "SELECT * FROM users WHERE unique_id = ?"
            ).use { ps ->
                ps.setString(1, uuid.toString())
                ps.executeQuery().use { rs ->
                    if (rs.next()) rs.toUser() else null
                }
            }
        }

    override fun findByName(name: String): DiscordUser? =
        ds.connection.use { con ->
            con.prepareStatement(
                "SELECT * FROM users WHERE name = ?"
            ).use { ps ->
                ps.setString(1, name)
                ps.executeQuery().use { rs ->
                    if (rs.next()) rs.toUser() else null
                }
            }
        }

    override fun findByDiscord(discordId: Long): DiscordUser? =
        ds.connection.use { con ->
            con.prepareStatement(
                "SELECT * FROM users WHERE discord_id = ?"
            ).use { ps ->
                ps.setLong(1, discordId)
                ps.executeQuery().use { rs ->
                    if (rs.next()) rs.toUser() else null
                }
            }
        }

    override fun findAll(): List<DiscordUser> =
        ds.connection.use { con ->
            con.prepareStatement(
                "SELECT * FROM users ORDER BY created_at"
            ).use { ps ->
                ps.executeQuery().use { rs ->
                    buildList {
                        while (rs.next()) {
                            add(rs.toUser())
                        }
                    }
                }
            }
        }

    override fun delete(uuid: UUID) {
        ds.connection.use { con ->
            con.prepareStatement(
                "DELETE FROM users WHERE unique_id = ?"
            ).use {
                it.setString(1, uuid.toString())
                it.executeUpdate()
            }
        }
    }

    private fun ResultSet.toUser() = DiscordUser(
        uniqueId = UUID.fromString(getString("unique_id")),
        name = getString("name"),
        discordId = getLong("discord_id"),
        minutes = getInt("minutes"),
        rewardedAt = getTimestamp("rewarded_at")?.toInstant(),
        createdAt = getInstant("created_at")
    )
}