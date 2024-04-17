package de.bypixeltv.notesk

import ch.njol.skript.Skript
import ch.njol.skript.SkriptAddon
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer
import de.bypixeltv.notesk.commands.Commands
import de.bypixeltv.notesk.tasks.UpdateCheck
import de.bypixeltv.notesk.utils.GetVersion
import de.bypixeltv.notesk.utils.UpdateChecker
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException


class Main : KSpigot() {

    private val miniMessages = MiniMessage.miniMessage()

    private var instance: Main? = null
    private var addon: SkriptAddon? = null

    companion object {
        lateinit var INSTANCE: Main
        var songPlayers = HashMap<Player, SongPlayer>()
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

        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>╔═════════════════════════════════════════════════════╗</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║     <grey>Enabling NoteSK <color:#43fa00>v1.0...</color></grey>                         ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║     <aqua>Made by byPixelTV</aqua>                               ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║     <grey>Github:</grey> <aqua>https://github.com/byPixelTV/NoteSK</aqua>     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#43fa00>╚═════════════════════════════════════════════════════╝</color>"))
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <aqua>Successfully enabled NoteSK v${this.description.version}!</aqua>"))
        Metrics(this, 21632)
        val dir: File = this.dataFolder
        if (!dir.exists()) {
            server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <color:#43fa00>Creating NoteSK folder...</color>"))
            dir.mkdirs()
        }
        val songsDir = File(this.dataFolder, "songs")
        if (!songsDir.exists()) {
            server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <color:#43fa00>Creating songs folder...</color>"))
            songsDir.mkdirs()
        }

        val githubVersion = GetVersion().getLatestAddonVersion()?.replace("v", "")?.toDouble()
        if (githubVersion != null) {
            if (githubVersion > this.description.version.replace("v", "").toDouble()) {
                server.consoleSender.sendMessage(" ")
                server.consoleSender.sendMessage(" ")
                server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <color:#43fa00>There is an update available for NoteSK!</color> <aqua>You're on version <yellow>v${this.description.version}</yellow> and the latest version is <yellow>$githubVersion</yellow></aqua>!\n\n<color:#43fa00>Download the latest version here:</color>\n<blue>https://github.com/byPixelTV/NoteSK/releases</blue> <aqua>"))
                server.consoleSender.sendMessage(" ")
                server.consoleSender.sendMessage(" ")
            } else {
                if (githubVersion == this.description.version.replace("v", "").toDouble()) {
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <color:#43fa00>You're on the latest version of NoteSK!</color> <aqua>Version <yellow>v${this.description.version}</yellow></aqua>"))
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(" ")
                } else if (githubVersion < this.description.version.replace("v", "").toDouble()) {
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <color:#ff0000>You're running a development version of NoteSK! Please note that this version may contain bugs!</color> <aqua>Version <color:#ff0000>v${this.description.version}</color> > <color:#43fa00>${GetVersion().getLatestAddonVersion()}</color></aqua>"))
                    server.consoleSender.sendMessage(" ")
                    server.consoleSender.sendMessage(" ")
                }
            }
        } else {
            server.consoleSender.sendMessage(" ")
            server.consoleSender.sendMessage(" ")
            server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <color:#ff0000>Unable to fetch the latest version from Github!</color> <aqua>Are you rate limited?</aqua>"))
            server.consoleSender.sendMessage(" ")
            server.consoleSender.sendMessage(" ")
        }

        UpdateChecker
        UpdateCheck
    }

    override fun load() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true).verboseOutput(true))
        Commands()
    }

    @Suppress("DEPRECATION")
    override fun shutdown() {
        CommandAPI.onDisable()
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>╔═════════════════════════════════════════════════════╗</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║     <grey>Disabling NoteSK <color:#ff0000>v1.0...</color></grey>                        ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║     <aqua>Made by byPixelTV</aqua>                               ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║     <grey>Github:</grey> <aqua>https://github.com/byPixelTV/NoteSK</aqua>     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>║                                                     ║</color>"))
        server.consoleSender.sendMessage(miniMessages.deserialize("<color:#ff0000>╚═════════════════════════════════════════════════════╝</color>"))
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(" ")
        server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<aqua>NoteSK</aqua>]</grey> <aqua>Successfully disabled NoteSK v${this.description.version}!</aqua>"))
    }
}