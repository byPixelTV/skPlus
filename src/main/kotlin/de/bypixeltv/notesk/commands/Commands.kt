package de.bypixeltv.notesk.commands

import ch.njol.skript.Skript
import ch.njol.skript.util.Version
import com.google.gson.Gson
import com.google.gson.JsonObject
import de.bypixeltv.notesk.Main
import de.bypixeltv.notesk.utils.UpdateChecker
import de.bypixeltv.notesk.utils.UpdateChecker.Companion.getLatestReleaseVersion
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

class Commands {
    private val miniMessages = MiniMessage.miniMessage()

    @Suppress("UNUSED", "DEPRECATION")
    val command = commandTree("notesk") {
        withPermission("notesk.admin")
        literalArgument("info") {
            withPermission("notesk.admin.info")
            anyExecutor { player, _ ->
                val addonMessages = Skript.getAddons().mapNotNull { addon ->
                    val name = addon.name
                    if (!name.contains("NoteSK")) {
                        "<grey>-</grey> <aqua>$name</aqua> <yellow>v${addon.plugin.description.version}</yellow>"
                    } else {
                        null
                    }
                }

                val addonsList =
                    if (addonMessages.isNotEmpty()) addonMessages.joinToString("\n") else "<color:#ff0000>No other addons found</color>"
                player.sendMessage(
                    miniMessages.deserialize(
                        "<dark_grey>--- <aqua>NoteSK</aqua> <grey>Info:</grey> ---</dark_grey>\n\n<grey>NoteSK Version: <aqua>${Main.INSTANCE.description.version}</aqua>\nSkript Version: <aqua>${Skript.getInstance().description.version}</aqua>\nServer Version: <aqua>${Main.INSTANCE.server.minecraftVersion}</aqua>\nServer Implementation: <aqua>${Main.INSTANCE.server.version}</aqua>\nAddons:\n$addonsList</grey>"
                    )
                )
            }
        }
        literalArgument("docs") {
            withPermission("notesk.admin.docs")
            anyExecutor { player, _ ->
                player.sendMessage(
                    miniMessages.deserialize(
                        "<dark_grey>[<gradient:#6900FF:#CF30FF:#6900FF>NoteSK</gradient>]</dark_grey> <grey><aqua>Documentation</aqua> for <aqua>NoteSK:</aqua></grey>\n<grey>-</grey> <click:open_url:'https://skripthub.net/docs/?addon=NoteSK'><aqua>SkriptHub</aqua> <dark_grey>(<aqua>Click me!</aqua>)</dark_grey></click>\n<grey>-</grey> <click:open_url:'https://docs.skunity.com/syntax/search/addon:notesk'><aqua>SkUnity</aqua> <dark_grey>(<aqua>Click me!</aqua>)</dark_grey></click>"
                    )
                )
            }
        }
        literalArgument("version") {
            withPermission("notesk.admin.version")
            anyExecutor { player, _ ->
                val currentVersion = Main.INSTANCE.description.version
                val updateVersion = UpdateChecker(Main.INSTANCE).getUpdateVersion(currentVersion)

                getLatestReleaseVersion { version ->
                    val plugVer = Version(Main.INSTANCE.description.version)
                    val curVer = Version(version)
                    val url = URL("https://api.github.com/repos/byPixelTV/NoteSK/releases/latest")
                    val reader = BufferedReader(InputStreamReader(url.openStream()))
                    val jsonObject = Gson().fromJson(reader, JsonObject::class.java)
                    var tagName = jsonObject["tag_name"].asString
                    tagName = tagName.removePrefix("v")
                    if (curVer.compareTo(plugVer) <= 0) {
                        player.sendMessage(miniMessages.deserialize("<dark_grey>[<gradient:#6900FF:#CF30FF:#6900FF>NoteSK</gradient>]</dark_grey> <green>The plugin is up to date!</green>"))
                    } else {
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
        literalArgument("reload") {
            withPermission("notesk.admin.reload")
            anyExecutor { player, _ ->
                Main.INSTANCE.reloadConfig()
                val path = Paths.get("/plugins/NoteSK/config.yml")
                if (Files.exists(path)) {
                    Main.INSTANCE.saveConfig()
                } else {
                    Main.INSTANCE.saveDefaultConfig()
                }
                player.sendMessage(miniMessages.deserialize("<dark_grey>[<gradient:#6900FF:#CF30FF:#6900FF>NoteSK</gradient>]</dark_grey> <color:#43fa00>Successfully reloaded the config!</color>"))
            }
        }
    }
}