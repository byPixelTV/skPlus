package de.bypixeltv.notesk.utils

import de.bypixeltv.notesk.Main
import net.axay.kspigot.event.listen
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.player.PlayerJoinEvent

object UpdateChecker {
    private val miniMessages = MiniMessage.miniMessage()


    @Suppress("DEPRECATION", "Unused")
    val joinEvent = listen<PlayerJoinEvent> {
        val player = it.player
        if (Main.INSTANCE.config.getBoolean("update-checker")) {
            if (player.hasPermission("notesk.admin.version") || player.isOp) {
                val githubVersion = GetVersion().getLatestAddonVersion()?.replace("v", "")?.toDouble()
                if (githubVersion != null) {
                    if (githubVersion > Main.INSTANCE.description.version.replace("v", "").toDouble()) {
                        player.sendMessage(miniMessages.deserialize("<dark_grey>[<gradient:#6900FF:#CF30FF:#6900FF>NoteSK</gradient>]</dark_grey> <color:#43fa00>There is an update available for NoteSK!</color> <aqua>You're on version <yellow>${Main.INSTANCE.description.version}</yellow> and the latest version is <yellow>$githubVersion</yellow></aqua>!\n\n<color:#43fa00>Download the latest version here:</color>\n<blue>https://github.com/byPixelTV/NoteSK/releases</blue><aqua>"))
                    } else {
                        if (githubVersion < Main.INSTANCE.description.version.replace("v", "").toDouble()) {
                            player.sendMessage(miniMessages.deserialize("<dark_grey>[<gradient:#6900FF:#CF30FF:#6900FF>NoteSK</gradient>]</dark_grey> <color:#ff0000>You're running a development version of NoteSK! Please note that this version may contain bugs!</color> <aqua>Version <color:#ff0000>${Main.INSTANCE.description.version}</color> > <color:#43fa00>${GetVersion().getLatestAddonVersion()}</color></aqua>"))
                        }
                    }
                } else {
                    player.sendMessage(miniMessages.deserialize("<dark_grey>[<gradient:#6900FF:#CF30FF:#6900FF>NoteSK</gradient>]</dark_grey> <color:#ff0000>Unable to fetch the latest version from Github!</color> <aqua>Are you rate limited?</aqua>"))
                }
            }
        }
    }
}