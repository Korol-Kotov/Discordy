package me.korolkotov.discordy.database.repository

import me.korolkotov.discordy.database.dao.UserDao

data class DiscordRepository(
    val userDao: UserDao
)