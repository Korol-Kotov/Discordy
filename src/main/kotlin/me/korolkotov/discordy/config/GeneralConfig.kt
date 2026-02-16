package me.korolkotov.discordy.config

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration

class GeneralConfig(yaml: YamlConfiguration) {
    val plugin = PluginConfig(yaml.getConfigurationSection("plugin")!!)
    val bot = BotConfig(yaml.getConfigurationSection("bot")!!)
}

class PluginConfig(section: ConfigurationSection) {
    val language = section.getString("language")!!
    val debug = section.getBoolean("debug")
}

class BotConfig(section: ConfigurationSection) {
    val token = section.getString("token")!!
    val code = BotCodeConfig(section.getConfigurationSection("code")!!)
    val echoChannels = section.getStringList("echo-channels").mapNotNull { it.toLongOrNull() }

    val privileges = BotPrivilegesConfig(section.getConfigurationSection("privileges")!!)
    val voice = BotVoiceConfig(section.getConfigurationSection("voice")!!)
}

class BotCodeConfig(section: ConfigurationSection) {
    val length = section.getInt("length")
    val lifetime = section.getInt("lifetime")
    val symbols = section.getString("symbols")!!
}

class BotPrivilegesConfig(private val section: ConfigurationSection) {
    fun getPrivileges(): List<PrivilegeConfig> {
        return section.getKeys(false).mapNotNull { getPrivilege(it) }
    }

    fun getPrivilege(id: String): PrivilegeConfig {
        val privilegeSection = section.getConfigurationSection(id)!!
        return PrivilegeConfig(privilegeSection)
    }
}

class PrivilegeConfig(section: ConfigurationSection) {
    val subgroups = section.getStringList("subgroups")
    val role = section.getLong("role")
    val commands = section.getStringList("commands")
}

class BotVoiceConfig(section: ConfigurationSection) {
    val period = section.getInt("period")
    val channels = section.getStringList("channels").mapNotNull { it.toLongOrNull() }
    val commands = section.getStringList("commands")
}