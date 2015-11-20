package gg.obsidian.discordbridge

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

class DiscordBridge : JavaPlugin(), Listener {

    var serverID: String = ""
    var channel: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""

    var connection: DiscordConnection? = null

    override fun onEnable() {
        updateConfig(description.version)

        this.serverID = config.getString("settings.server-id")
        this.channel = config.getString("settings.channel")
        this.username = config.getString("settings.username")
        this.email = config.getString("settings.email")
        this.password = config.getString("settings.password")

        this.connection = DiscordConnection(this)

        server.scheduler.runTaskAsynchronously(this, connection)
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onChat(event: AsyncPlayerChatEvent) {
        send(event.player.name, event.message)
    }

    fun send(name: String, message: String) {
        connection!!.send(name, message)
    }

    fun updateConfig(version: String) {
        this.saveDefaultConfig()
        config.options().copyDefaults(true)
        config.set("version", version)
        saveConfig()
    }
}