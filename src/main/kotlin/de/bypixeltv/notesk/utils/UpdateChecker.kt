package de.bypixeltv.notesk.utils

import ch.njol.skript.util.Version
import com.google.gson.Gson
import com.google.gson.JsonObject
import de.bypixeltv.notesk.Main
import net.axay.kspigot.extensions.server
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

/*
    This got ported into Kotlin by @byPixelTV and was originally written in Java by ShaneBeee
    You can find this code at https://github.com/ShaneBeee/SkBee/blob/master/src/main/java/com/shanebeestudios/skbee/api/util/UpdateChecker.java
    Checkout https://github.com/ShaneBeee/SkBee
*/

class UpdateChecker(private val plugin: Main) : Listener {

    companion object {
        private var UPDATE_VERSION: Version? = null

        fun checkForUpdate(pluginVersion: String) {
            val miniMessages = MiniMessage.miniMessage()
            server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<dark_purple>NoteSK</dark_purple>]</grey> Checking for updates..."))
            getLatestReleaseVersion { version ->
                val plugVer = Version(pluginVersion)
                val curVer = Version(version)
                if (curVer.compareTo(plugVer) <= 0) {
                    server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<dark_purple>NoteSK</dark_purple>]</grey> <green>The plugin is up to date!</green>"))
                } else {
                    server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<dark_purple>NoteSK</dark_purple>]</grey> <red>The plugin is not up to date!</red>"))
                    server.consoleSender.sendMessage(miniMessages.deserialize(" - Current version: <red>v${pluginVersion}</red>"))
                    server.consoleSender.sendMessage(miniMessages.deserialize(" - Available update: <green>v${version}</green>"))
                    server.consoleSender.sendMessage(miniMessages.deserialize(" - Download available at: <dark_purple>https://github.com/byPixelTV/NoteSK/releases</dark_purple>"))
                    UPDATE_VERSION = curVer
                }
            }
        }

        fun getLatestReleaseVersion(consumer: Consumer<String>) {
            val miniMessages = MiniMessage.miniMessage()
            try {
                val url = URL("https://api.github.com/repos/byPixelTV/NoteSK/releases/latest")
                val reader = BufferedReader(InputStreamReader(url.openStream()))
                val jsonObject = Gson().fromJson(reader, JsonObject::class.java)
                var tagName = jsonObject["tag_name"].asString
                tagName = tagName.removePrefix("v")
                consumer.accept(tagName)
            } catch (e: IOException) {
                server.consoleSender.sendMessage(miniMessages.deserialize("<grey>[<dark_purple>NoteSK</dark_purple>]</grey> <red>Checking for updates failed!</red>"))
            }
        }
    }

    @Suppress("RedundantSamConstructor")
    fun getUpdateVersion(currentVersion: String): CompletableFuture<Version> {
        val future = CompletableFuture<Version>()
        if (UPDATE_VERSION != null) {
            future.complete(UPDATE_VERSION)
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, Runnable {
                getLatestReleaseVersion(Consumer { version ->
                    val plugVer = Version(currentVersion)
                    val curVer = Version(version)
                    if (curVer.compareTo(plugVer) <= 0) {
                        future.cancel(true)
                    } else {
                        UPDATE_VERSION = curVer
                        future.complete(UPDATE_VERSION)
                    }
                })
            })
        }
        return future
    }
}