package de.bypixeltv.notesk.utils

import de.bypixeltv.notesk.Main
import net.axay.kspigot.event.listen
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent

object IngameUpdateChecker {
    private val miniMessages = MiniMessage.miniMessage()

    @Suppress("DEPRECATION", "UNUSED")
    val joinEvent = listen<PlayerJoinEvent> {
        val player = it.player
        if (Main.INSTANCE.config.getBoolean("update-checker")) {
            if (player.hasPermission("notesk.admin.version") || player.isOp) {
                val currentVersion = Main.INSTANCE.description.version
                val updateVersion = UpdateChecker(Main.INSTANCE).getUpdateVersion(currentVersion)

                Bukkit.getScheduler().runTaskLater(Main.INSTANCE, Runnable {
                    updateVersion.thenApply { version ->
                        player.sendMessage(miniMessages.deserialize("<dark_grey>[<gradient:#6900FF:#CF30FF:#6900FF>NoteSK</gradient>]</dark_grey> update available: <green>$version</green>"))
                        player.sendMessage(miniMessages.deserialize("<dark_grey>[<gradient:#6900FF:#CF30FF:#6900FF>NoteSK</gradient>]</dark_grey> download at <aqua><click:open_url:'https://github.com/byPixelTV/NoteSK/releases'>https://github.com/byPixelTV/NoteSK/releases</click></aqua>"))
                        true
                    }
                }, 30)
            }
        }
    }
}