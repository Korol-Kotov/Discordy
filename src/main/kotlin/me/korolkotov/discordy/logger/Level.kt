package me.korolkotov.discordy.logger

enum class Level {
    DEBUG,
    INFO,
    WARN,
    ERROR;

    val prefix = "[${this.name.uppercase()}]"
}