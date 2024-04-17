package de.bypixeltv.notesk

import ch.njol.skript.Skript
import ch.njol.skript.SkriptAddon
import de.bypixeltv.notesk.commands.Commands
import de.bypixeltv.notesk.tasks.UpdateCheck
import de.bypixeltv.notesk.utils.GetVersion
import de.bypixeltv.notesk.utils.UpdateChecker
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.text.minimessage.MiniMessage
import java.io.IOException

class Main : KSpigot() {

    private val miniMessages = MiniMessage.miniMessage()

    var instance: Main? = null
    private var addon: SkriptAddon? = null

    companion object {
        lateinit var INSTANCE: Main
    }

    @Suppress("DEPRECATION")
    override fun startup() {
        saveDefaultConfig()

        INSTANCE = this
        this.instance = this
        this.addon = Skript.registerAddon(this)
        val localAddon = this.addon
        try {
            localAddon?.loadClasses("de.bypixeltv.notesk", "elements")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>Enabling NoteSK v${this.description.version}...</color>"))
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00> _   _         _          ____   _  __</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>| \\ | |  ___  | |_   ___ / ___| | |/ /</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>|  \\| | / _ \\ | __| / _ \\\\___ \\ | ' / </color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>| |\\  || (_) || |_ |  __/ ___) || . \\ </color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>|_| \\_| \\___/  \\__| \\___||____/ |_|\\_\\</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<yellow>Made by byPixelTV</yellow>"))
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(miniMessages.deserialize("<aqua>Successfully enabled NoteSK v${this.description.version}!</aqua>"))


        val githubVersion = GetVersion().getLatestAddonVersion()?.replace("v", "")?.toDouble()
        if (githubVersion != null) {
            if (githubVersion > this.description.version.replace("v", "").toDouble()) {
                server.consoleSender.sendMessage(" ")
                server.consoleSender.sendMessage(" ")
                server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>There is an update available for NoteSK!</color> <aqua>You're on version <yellow>v${this.description.version}</yellow> and the latest version is <yellow>$githubVersion</yellow></aqua>!\n\n<color:#43fa00>Download the latest version here:</color> <blue>https://github.com/byPixelTV/NoteSK/releases</blue> <aqua>"))
                server.consoleSender.sendMessage(" ")
                server.consoleSender.sendMessage(" ")
            } else {
                if (githubVersion == this.description.version.replace("v", "").toDouble()) {
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>You're on the latest version of NoteSK!</color> <aqua>Version <yellow>v${this.description.version}</yellow></aqua>"))
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(" ")
                } else if (githubVersion < this.description.version.replace("v", "").toDouble()) {
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>You're running a development version of NoteSK! Please note that this version may contain bugs!</color> <aqua>Version <color:#ff0000>v${this.description.version}</color> > <color:#43fa00>${GetVersion().getLatestAddonVersion()}</color></aqua>"))
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(" ")
                }
            }
        } else {
            server.consoleSender.sendMessage(" ")
            server.consoleSender.sendMessage(" ")
            server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>Unable to fetch the latest version from Github!</color> <aqua>Are you rate limited?</aqua>"))
            server.consoleSender.sendMessage(" ")
            server.consoleSender.sendMessage(" ")
        }

        UpdateChecker
        UpdateCheck

        val metrics: Metrics = Metrics(this, 21526)
    }

    override fun load() {
        server.consoleSender.sendMessage(miniMessages.deserialize("<blue>Loading NoteSK...</blue>"))
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true).verboseOutput(true))
        Commands()
    }

    override fun shutdown() {
        CommandAPI.onDisable()
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>Disabling NoteSK v${this.description.version}...</color>"))
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00> _   _         _          ____   _  __</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>| \\ | |  ___  | |_   ___ / ___| | |/ /</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>|  \\| | / _ \\ | __| / _ \\\\___ \\ | ' / </color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>| |\\  || (_) || |_ |  __/ ___) || . \\ </color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>|_| \\_| \\___/  \\__| \\___||____/ |_|\\_\\</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<yellow>Made by byPixelTV</yellow>"))
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(miniMessages.deserialize("<aqua>Successfully disabled NoteSK v${this.description.version}!</aqua>"))
    }

    fun getMainInstance(): Main? {
        return instance
    }

    fun getAddonInstance(): SkriptAddon? {
        return addon
    }
}