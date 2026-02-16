package me.korolkotov.discordy.discord

import net.dv8tion.jda.api.JDABuilder

class Bot(token: String) {
    companion object {
        lateinit var instance: Bot private set
    }

    val jda = JDABuilder.createLight(token).build()

    init {
        instance = this
    }
}